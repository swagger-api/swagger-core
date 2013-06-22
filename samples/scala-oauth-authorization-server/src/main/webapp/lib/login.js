$(document).ready(function(){
  // Additional password confirmation dialog
  function showConfirmForm(animate) {
    $('input[name=accept]').val('Allow');
    $('.secondary_form').css({visibility:"visible"})
    $('.initial_form').hide();

    if(animate)
      $('.initial_form, .secondary_form').addClass('animated')
    else
      $('.initial_form, .secondary_form').removeClass('animated')

    $('form').addClass('confirm');
    $('section').addClass('confirm');

    $('input[name=email]').focus();
    
  }

  function disableButton(el) {
    $(el).attr('disabled','disabled');
    $(el).addClass('disabled');
  }

  // Button behaviors
  $("button[name=deny]").click(function(e) {
    disableButton(this);
    e.preventDefault();

    $('input[name=accept]').val('Deny');
    $('form').submit();
  });

  $("button[name=allow]").click(function(e) {
    disableButton(this);
    e.preventDefault();

    showConfirmForm(true);
  });

  $("button[name=confirm]").click(function(e) {
    disableButton(this);
    e.preventDefault();

    $('form').submit();
  });  
});
