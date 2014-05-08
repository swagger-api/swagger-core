$( document ).ready(function() {
  $("table").addClass("table table-striped");
  hljs.initHighlightingOnLoad();
  console.log("foo")
});

function setActive(className, selectedItem) {
  $(className).removeClass("active");
  $(className + "-" + selectedItem).addClass("active");
}