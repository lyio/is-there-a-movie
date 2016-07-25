require "rexml/document"
require "json"
require "open-uri"

def vertx_start_async fut

 puts "started goodreads verticle"

 # JVM system properties via the Java API
 @KEY = Java::JavaLang::System.getProperty("goodreads")

 eb = $vertx.event_bus()

 eb.consumer("botomo.goodreads.lookup") { |message|
   query_goodreads message
 }

 fut.complete()
end

def query_goodreads(message)
  options = {
    'logActivity' => true,
    'defaultHost' => "www.goodreads.com"
  }
  client = $vertx.create_http_client(options)
  term = URI.encode(message.body)
  client.get_now("/search.xml?key=#{@KEY}&q=#{term}") do |response|
    puts "Received response with status code #{response.status_code()}"
    if response.status_code >= 300 then reply(false, response.status_message, message) end
    response.body_handler() do |buffer|
     @books = read_xml buffer
     reply(true, @books.to_json, message)
    end
  end
  ensure
    client.close
    # if instance variable was never defined there was an error sending the request
    if !@books then reply(false, "failed request", message) end
end

def read_xml(buffer)
  doc = REXML::Document.new buffer.to_string
  books = []
  doc.elements.each('GoodreadsResponse/search/results/work') do |work|
    elements = work.elements
    book = {}
    book[:title] = elements['best_book'].elements['title'].text
    book[:author] = elements['best_book'].elements['author'].elements['name'].text
    book[:year] = elements['original_publication_year'].text
    book[:image] = elements['best_book'].elements['image_url'].text
    books.push book
  end
  books
end

def reply(successful, payload, message)
     puts "replying #{payload}"
     response = {
            "payload" => payload,
            "state" => successful
          }
     message.reply(response.to_json)
end
