---
layout: default
title: TODO - need to do
selectedMenu: service
---
<br>
<blockquote>
	<p id="quote"><p>
</blockquote>

<script> 

//Satirical Punch Line Script- by javascriptkit.com (text by Colin Lingle)
//Visit JavaScript Kit (http://javascriptkit.com) for script
//Credit must stay intact for use

quotes = new Array();
quotes.push("Warning: Dates in Calendar are closer than they appear.");
quotes.push("Daddy, why doesn't this magnet pick up this floppy disk?");
quotes.push("Give me ambiguity or give me something else.");
quotes.push("I.R.S.: We've got what it takes to take what you've got!");
quotes.push("Make it idiot proof and someone will make a better idiot.");
quotes.push("Always remember you're unique, just like everyone else.");
quotes.push("Save the whales, collect the whole set");
quotes.push("A flashlight is a case for holding dead batteries.");
quotes.push("Lottery: A tax on people who are bad at math.");
quotes.push("There's too much blood in my caffeine system.");
quotes.push("I wont rise to the occaasion, but I'll slide over to it.");
quotes.push("Ever notice how fast Windows runs?  Neither did I.");
quotes.push("Double your drive space - delete Windows!");
quotes.push("What is a free gift ? Aren't all gifts free?");
quotes.push("Puritanism: The haunting fear that someone, somewhere may be happy.");
quotes.push("Consciousness: that annoying time between naps.");
quotes.push("Oops. My brain just hit a bad sector.");
quotes.push("I used to have a handle on life, then it broke.");
quotes.push("A pedestrian hit me and went under my car.");
quotes.push("Better to understand a little than to misunderstand a lot.");
quotes.push("When there's a will, I want to be in it.");
quotes.push("We have enough youth, how about a fountain of SMART?");
quotes.push("All generalizations are false, including this one.");
quotes.push("Change is inevitable, except from a vending machine.");
quotes.push("C program run.  C program crash.  C programmer quit.");
quotes.push("90% of all statistics are made up");
quotes.push("Apple copyright 6024 b.c., Adam & Eve");
quotes.push("Apple Copyright 1767, Sir Isaac Newton.");
quotes.push("Beam me aboard, Scotty..... Sure. Will a 2x10 do?");
quotes.push("Beulah, peel me a grape.");
quotes.push("Bother, said Pooh as the brakes went out!");
quotes.push("Build a watch in 179 easy steps - by C. Forsberg.");
quotes.push("Calvin, we will not have an anatomically correct snowman!");
quotes.push("Careful.  We don't want to learn from this. -- Calvin");
quotes.push("Energizer Bunny Arrested! Charged with battery.");

function randomdisplay(){
	randomquote=quotes[Math.floor(Math.random()*quotes.length)];
	console.log(randomquote);
	$('#quote').html(randomquote);
}

setTimeout("randomdisplay()",100)

</script>