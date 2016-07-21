require "rexml/document"
require "json"
require "open-uri"

def vertx_start

 puts "started goodreads verticle"

 eb = $vertx.event_bus()

 eb.consumer("goodreads.lookup") { |message|
   puts "#{message.body} received"
   query_goodreads message
 }
end

def query_goodreads(message)
  options = {
    'logActivity' => true,
    'defaultHost' => "www.goodreads.com"
  }
  client = $vertx.create_http_client(options)
  term = URI.encode(message.body)
  client.get_now("/search?key=XxgFKnqX7pgEdiGcT3os8Q&q=#{term}") { |response|
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
        book[:image] = elements['small_image_url'].text
        books.push book
        puts book
      end
      message.reply(books.to_json)
    end
  }
  client.close
end
