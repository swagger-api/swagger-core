module Views
  def view( viewName )
  		%w( html json ).each do |extension|
      	path = File.join('public', "#{viewName.to_s}.#{extension.to_s}")
      	if File.exists? path
      		return File.read path
      	end
      end
    end
end
