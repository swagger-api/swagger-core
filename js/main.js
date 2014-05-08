$( document ).ready(function() {
  $("table").addClass("table table-striped");
  hljs.initHighlightingOnLoad();
  $('.toc').toc({ listType: 'ul', showSpeed: 0 })
});

function setActive(className, selectedItem) {
  $(className).removeClass("active");
  $(className + "-" + selectedItem).addClass("active");
}