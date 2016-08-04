QUnit.module("wng.address");

QUnit.test("wngAddress", function (assert) {
    var address = new WngAddress();
    assert.equal(address.set("WNG-XK4R-7VJU-6EQG-7R335"), true, "valid address");
    assert.equal(address.toString(), "WNG-XK4R-7VJU-6EQG-7R335", "address");
    assert.equal(address.set("WNG-XK4R-7VJU-6EQG-7R336"), false, "invalid address");
});
