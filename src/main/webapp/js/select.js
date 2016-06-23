// refresh a part of the HTML specified by the given ID,
// by using the contents fetched from the given URL.
/*
 * The MIT License
 * 
 * Copyright (c) 2004-2010, Sun Microsystems, Inc., Kohsuke Kawaguchi,
 * Daniel Dyer, Yahoo! Inc., Alan Harder, InfraDNA, Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
//
//
// JavaScript for customized select control
//

// Multi-select and indented-select url binding
function openUrl(a){
    window.open(a.href);
}

function updateSelectedItemUrl(evt, select){
	var ch = evt.memo.chosen;
	
	var results_data = ch.results_data;
	var search_results = ch.search_results;
	var form_field = ch.form_field;
	
		
	// Fill url for selected items when it is single select
	/**/
	var sel = ch.selected_item;
	var span = sel.childNodes[0];
	var opt = form_field.options[form_field.selectedIndex];
	span.innerHTML = span.innerHTML.replace(/&nbsp;/g, ""); 
	if(opt.url && span.getElementsByTagName("a").length == 0){
		span.innerHTML = "<a target=\"_blank\" href=\"" + opt.url + "\" onclick=\"openUrl(this);return false;\">" + span.innerHTML + "</a>";
	}
}

function buildSelectOption(data){
	var option = new Option(data.name, data.value);
	
	// html = indent + name + url(optionally)
	var html = data.name;
	if(data.url){
		option.url = data.url;
		html = "<span>" + "<a target=\"_blank\" href=\"" + data.url + "\" onclick=\"openUrl(this);return false;\">" + html + "</a>" + "</span>";
	}
	
	// Indent spances
	var indent = "";
	if(data.indent && data.indent > 0){
		indent = Array(data.indent + 1).join("&nbsp;&nbsp;&nbsp;");
	}
	
	option.innerHTML = indent + html;
	return option;	
}

/**
	Indented select control
**/

function rebuildChosenSingleSelect(listBox){
	// remove chosen element if existed
	var cdiv = listBox.next();
	if(cdiv){
		cdiv.remove();
	}
	
	var config = {
	  '.chosen-select'           : {width:"100%", disable_search:true},
	  '.chosen-select-deselect'  : {allow_single_deselect:true},
	  '.chosen-select-no-single' : {disable_search_threshold:10},
	  '.chosen-select-no-results': {no_results_text: "Oops, nothing found!"},
	  '.chosen-select-width'     : {width: "95%"}
	}
	
	// Hook up notifications
	listBox.on('chosen:ready', updateSelectedItemUrl);
	listBox.on('chosen:showing_dropdown', updateSelectedItemUrl);
	listBox.on('chosen:hiding_dropdown', updateSelectedItemUrl);
	
	return new Chosen(listBox,config[".chosen-select"]);
}

// send async request to the given URL (which will send back serialized ListBoxModel object),
// then use the result to fill the list box.
function updateSingleSelectListBox(listBox,url,config) {
    config = config || {};
    config = object(config);
    var originalOnSuccess = config.onSuccess;
    config.onSuccess = function(rsp) {
        var l = $(listBox);
        var currentSelection = l.value;

        // clear the contents
        while(l.length>0)   l.options[0] = null;

        var selectionSet = false; // is the selection forced by the server?
        var possibleIndex = null; // if there's a new option that matches the current value, remember its index
        var opts = eval('('+rsp.responseText+')').values;
        for( var i=0; i<opts.length; i++ ) {
			l.options[i] = buildSelectOption(opts[i]);
			
			if(opts[i].selected) {
                l.selectedIndex = i;
                selectionSet = true;
            }
            if (opts[i].value==currentSelection)
                possibleIndex = i;
        }

        // if no value is explicitly selected by the server, try to select the same value
        if (!selectionSet && possibleIndex!=null)
            l.selectedIndex = possibleIndex;
			
		rebuildChosenSingleSelect(l);

        if (originalOnSuccess!=undefined)
            originalOnSuccess(rsp);
    },
    config.onFailure = function(rsp) {
        // deleting values can result in the data loss, so let's not do that
//        var l = $(listBox);
//        l.options[0] = null;
    }

    new Ajax.Request(url, config);
}

Behaviour.specify("SELECT.dti-single-select", 'dti-single-select', 0, function(e) {
        // controls that this SELECT box depends on
        refillOnChange(e,function(params) {
            var value = e.value;
            updateSingleSelectListBox(e,e.getAttribute("fillUrl"),{
                parameters: params,
                onSuccess: function() {
                    if (value=="") {
                        // reflect the initial value. if the control depends on several other SELECT.select,
                        // it may take several updates before we get the right items, which is why all these precautions.
                        var v = e.getAttribute("value");
                        if (v) {
                            e.value = v;
                            if (e.value==v) e.removeAttribute("value"); // we were able to apply our initial value
                        }
                    }

                    fireEvent(e,"filled"); // let other interested parties know that the items have changed

                    // if the update changed the current selection, others listening to this control needs to be notified.
                    if (e.value!=value) fireEvent(e,"change");
                }
            });
        });
});

/**
	Multi-select control
**/
function rebuildChosenMultiSelect(listBox){
	// remove chosen element if existed
	var cdiv = listBox.next();
	if(cdiv){
		cdiv.remove();
	}
	
	var config = {
	  '.chosen-select'           : {width:"100%"},
	  '.chosen-select-deselect'  : {allow_single_deselect:true},
	  '.chosen-select-no-single' : {disable_search_threshold:10},
	  '.chosen-select-no-results': {no_results_text: "Oops, nothing found!"},
	  '.chosen-select-width'     : {width: "95%"}
	}
	return new Chosen(listBox,config[".chosen-select"]);	
}

// send async request to the given URL (which will send back serialized ListBoxModel object),
// then use the result to fill the list box.
function updateMultiSelectListBox(listBox,url,config) {
    config = config || {};
    config = object(config);
    var originalOnSuccess = config.onSuccess;
    config.onSuccess = function(rsp) {
        var l = $(listBox);
        var currentSelection = [];
		for(var j = 0; j < l.selectedOptions.length; j++){
			currentSelection.push(l.selectedOptions[j].value);
		}

        // clear the contents
        while(l.childElementCount > 0)   l.removeChild(l.firstChild);

        var opts = eval('('+rsp.responseText+')').values;
        for( var i=0; i<opts.length; i++ ) {
			// Is it an option group
			if(opts[i].options){
				var group = document.createElement("OPTGROUP");
				group.label = opts[i].name;
				
				// Build sub items
				var subOpts = opts[i].options;
				for(var j = 0; j < subOpts.length; j++){
					var opt = buildSelectOption(subOpts[j]);
					group.appendChild(opt);
					
					if (currentSelection.indexOf(subOpts[j].value) != -1){
						opt.selected = true;
					}					
				}
				
				l.appendChild(group);
			}else{
				var opt = buildSelectOption(opts[i]);
				l.appendChild(opt);
				if (currentSelection.indexOf(opts[i].value) != -1){
					opt.selected = true;
				}
			}
        }

	    // create chosen style multi-select
		rebuildChosenMultiSelect(l);
		
        if (originalOnSuccess!=undefined)
            originalOnSuccess(rsp);
    },
    config.onFailure = function(rsp) {
        // deleting values can result in the data loss, so let's not do that
//        var l = $(listBox);
//        l.options[0] = null;
    }

    new Ajax.Request(url, config);
}

Behaviour.specify("SELECT.dti-multi-select", 'dti-multi-select', 0, function(e) {
        // controls that this SELECT box depends on
        refillOnChange(e,function(params) {
            var value = e.value;
            updateMultiSelectListBox(e,e.getAttribute("fillUrl"),{
                parameters: params,
                onSuccess: function() {
                    if (value=="") {
                        // reflect the initial value. if the control depends on several other SELECT.select,
                        // it may take several updates before we get the right items, which is why all these precautions.
                        var v = e.getAttribute("value");
                        if (v) {
                            e.value = v;
                            if (e.value==v) e.removeAttribute("value"); // we were able to apply our initial value
                        }
                    }

                    fireEvent(e,"filled"); // let other interested parties know that the items have changed

                    // if the update changed the current selection, others listening to this control needs to be notified.
                    if (e.value!=value) fireEvent(e,"change");
                }
            });
        });
});