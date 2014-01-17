$( document ).ready(function() {
  $('#allow').click(function(){
    $('.secondary_form').css({display: 'block'})
  });

  $('#deny').click(function() {
    $('#accept').val("deny");
    $('form').submit();
  });
});