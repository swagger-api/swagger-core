// https://github.com/ghiculescu/jekyll-table-of-contents
(function($){
  $.fn.toc = function(options) {
    var defaults = {
      noBackToTopLinks: false,
      title: '<h2>TOC</h2>',
      listType: 'ol', // values: [ol|ul]
      showSpeed: 'slow'
    },
    settings = $.extend(defaults, options);
    
    var headers = $('h1, h2, h3, h4, h5, h6').filter(function() {
      // get all headers with an ID
      return this.id;
    }), output = $(this);
    if (!headers.length || headers.length < 3 || !output.length) {
      return;
    }
    
    var get_level = function(ele) { return parseInt(ele.nodeName.replace("H", ""), 10); }
    var highest_level = headers.map(function(_, ele) { return get_level(ele); }).get().sort()[0];
    var return_to_top = '<i class="icon-arrow-up back-to-top"> </i>';
    
    var level = get_level(headers[0]),
      this_level,
      html = settings.title + " <"+settings.listType+">";
    headers.on('click', function() {
      if (!settings.noBackToTopLinks) {
        window.location.hash = this.id;
      }
    })
    .addClass('clickable-header')
    .each(function(_, header) {
      this_level = get_level(header);
      if (!settings.noBackToTopLinks && this_level === highest_level) {
        $(header).addClass('top-level-header').after(return_to_top);
      }
      if (this_level === level) // same level as before; same indenting
        html += "<li><a href='#" + header.id + "'>" + header.innerHTML + "</a>";
      else if (this_level < level) // higher level than before; end parent ol
        html += "</li></"+settings.listType+"></li><li><a href='#" + header.id + "'>" + header.innerHTML + "</a>";
      else if (this_level > level) // lower level than before; expand the previous to contain a ol
        html += "<"+settings.listType+"><li><a href='#" + header.id + "'>" + header.innerHTML + "</a>";
      level = this_level; // update for the next one
    });
    html += "</"+settings.listType+">";
    if (!settings.noBackToTopLinks) {
      $(document).on('click', '.back-to-top', function() {
        $(window).scrollTop(0);
        window.location.hash = '';
      });
    }
    if (0 !== settings.showSpeed) {
      output.hide().html(html).show(settings.showSpeed);
    } else {
      output.html(html);
    }
  };
})(jQuery);