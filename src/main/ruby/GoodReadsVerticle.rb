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
    books = []
    response.body_handler() do |buffer|
      doc = REXML::Document.new buffer.to_string
      doc.elements.each('GoodreadsResponse/search/results/work') do |work|
        elements = work.elements
        book = {}
        book[:title] = elements['best_book'].elements['title'].text
        book[:author] = elements['best_book'].elements['author'].elements['name'].text
        book[:year] = elements['original_publication_year'].text
        book[:image] = elements['best_book'].elements['image_url'].text
        books.push book
      end
      response = {
        "payload" => books.to_json,
        "state" => true
      }
      message.reply(response.to_json)
    end
  end
  client.close
end
