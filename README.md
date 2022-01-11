# Test task for Global Logic

<b>1) Selenium WebDriver (UI)</b>

A) 
<li>Go to http://demowebshop.tricentis.com/</li>
<li>In the categories menu open Computer -> Desktops.</li>
<li>Set Display to "4" per page and check only 4 items displayed after that.</li>
<li>Sort "Price: High to Low", and click add to cart the most expensive item -> check the item is in the shopping cart.</li>
<p></p>
B) 
<li>Open http://demowebshop.tricentis.com/build-your-own-expensive-computer-2</li>
<li>Set Processor: Fast;</li>
<li>Set RAM: 8GB;</li>
<li>Select all available software;</li>
<li>Click "Add to cart" -> check the shopping cart has +1 item.</li>
<li>Open the Shopping cart -> check the item is there and the price is correct (according to the selected options on the item page).</li>
<li>Remove the item from the shopping cart.</li>
<p></p>
<b>2) API (preferrable to use REST Assured).</b>
<p></p>
<li>Create API test script(s) using https://restful-booker.herokuapp.com/apidoc/index.html</li>
<li>Perform /auth, use the returned token to create a new booking.</li>
<li>Get details of newly created booking, and ensure it has all details as were specified during its creation.</li>
<li>Update the booking details (e.g. update "totalprice" field).</li> 
<li>Get details of the updated booking, and ensure it has new details.</li>
<li>Get all bookings and check them have a newly created booking.</li>
<li>Delete the booking.</li>
