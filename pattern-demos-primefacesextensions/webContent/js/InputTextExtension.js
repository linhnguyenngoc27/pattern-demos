// Override the constructor and change behaviour.
// Note, that there also exists a client API which offers much functionality for existing widgets.
PrimeFaces.widget.InputText.prototype.init = function(cfg) {
	this.cfg = cfg;
	this.jq = $(PrimeFaces.escapeClientId(this.cfg.id));
    PrimeFaces.skinInput(this.jq);

     if(this.cfg.counter) {
     	this.counter = this.cfg.counter ? $(PrimeFaces.escapeClientId(this.cfg.counter)) : null;
        this.cfg.counterTemplate = this.cfg.counterTemplate||'{0}';
        this.updateCounter();

        if(this.counter) {
            var $this = this;
            this.jq.on('input.inputtext-counter', function(e) {
                $this.updateCounter();
            });
           }
        }
	
	// InputText is extended to check the maximum length not in characters but in bytes,
	// because some characters, such as umlauts, take more than one byte.
	this.normalizeNewlines(this.jq.val());
	var $this = this;
    this.jq.on('keyup.inputtextarea-maxlength', function(e) {
        if(e.currentTarget.maxLength > 0){
			$this.returnColor();
           	while(byteSize(e.currentTarget.value) > e.currentTarget.maxLength) {
           		e.currentTarget.value = e.currentTarget.value.slice(0,-1);
				$this.changeColor();
        	}
        }
     });
}

// This is example shows how to add new methods to a widget.
PrimeFaces.widget.InputText = PrimeFaces.widget.InputText.extend({
	// Add function to change color to red.
	changeColor: function() {
		this.jq.css('background-color', '#e38a8a'); 
	},
	// Add function to change color to green.
	returnColor: function() {
		this.jq.css('background-color', '#8ae3a3'); 
	}
});

// Helper function for byte size.
function byteSize(str) {
    return new Blob([str]).size;
}