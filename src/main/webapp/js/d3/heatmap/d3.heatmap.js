/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
(function () {
		d3.heatmap= function() {		
			  // static config
			  var margin = { top: 180, right: 600, bottom: 50, left: 100 },
			  
			  cellSize=20;
			  legendElementWidth = 20*3;
			  legendElementHeight = cellSize;
			  
			  var hoverCallback = [];
			  
			  // dynamic config
			  var _options;
			  
			  var _container;
			  var _rowLabels;
			  var _colLabels;
			  var _dataset;
			  
			  // Color & legend
			  var _colorBuckets;
			  var _legendColorLabels;
			  
			  // Callbacks
			  var _colorMapper;
			  var _tooltipHandler;
			  var _cellClickHandler;
			  
			  // Computed
			  var row_number;
			  var col_number;
			  var width;
			  var height;
			  
			  var enableRowLabelSorter
			  var enableColLabelSorter;
			  
			  this.init = function(options){
					_options = options; // remember options
					_container = options.container;
					_rowLabels = options.rowLabels;
					_colLabels = options.colLabels;
					_dataset = options.dataset;
					
					_colorBuckets = options.colorBuckets;
					_legendColorLabels = options.legendColorLabels;				
					
					_colorMapper = options.colorMapper;
					_tooltipHandler = options.tooltipHandler;
					_cellClickHandler = options.cellClickHandler;	
					
					row_number = _rowLabels.length;
					col_number = _colLabels.length;
					
					if(options.cellSize){
						cellSize = options.cellSize;
					}
					
					width = cellSize*col_number , // - margin.left - margin.right,
					height = cellSize*row_number; // - margin.top - margin.bottom,
					
					if(options.margin){
						margin = options.margin;
					}
					
					enableRowLabelSorter = options.enableRowLabelSorter;					
					enableColLabelSorter = options.enableColLabelSorter;
			  };
				
			  this.render = function() {
					  // add a tooltip container
					  d3.select(_container).append("div")
					    .attr("id", "tooltip")
						.attr("class", "hidden")
						.style("opacity", 0.85)
						.append("p")
						.append("span")
						.attr("id", "value")
						.style("font", "10px sans-serif")
						;
						
					  var colorScale = d3.scale.quantize()
						  .domain([1,100])
						  .range(_colorBuckets);
					  
					  // Draw the canvas
					  var svg = d3.select(_container).append("svg")
						  .attr("width", width + margin.left + margin.right)
						  .attr("height", height + margin.top + margin.bottom)
						  .append("g")
						  .attr("transform", "translate(" + margin.left + "," + margin.top + ")")
						  ;
						  
					  var rowSortOrder=false;
					  var colSortOrder=false;
					  
					  // Draw row labels
					  var rowLabels = svg.append("g")
						  .selectAll(".rowLabelg")
						  .data(_rowLabels)
						  .enter()
						  .append("text")
						  .text(function (d) { return d; })
						  .attr("x", 0)
						  .attr("y", function (d, i) { return (i+1) * cellSize; })
						  .style("text-anchor", "end")
						  .attr("transform", "translate(-6," + cellSize / 1.5 + ")")
						  .attr("class", function (d,i) { return "rowLabel mono r"+i;} ) 
						  .on("mouseover", function(d) {d3.select(this).classed("text-hover",true);})
						  .on("mouseout" , function(d) {d3.select(this).classed("text-hover",false);})
						  .on("click", function(d,i) { if(enableRowLabelSorter) { rowSortOrder=!rowSortOrder; sortbylabel("r",i,rowSortOrder);d3.select("#order").property("selectedIndex", 4).node().focus();;}})
						  ;

					  var colLabels = svg.append("g")
						  .selectAll(".colLabelg")
						  .data(_colLabels)
						  .enter()
						  .append("text")
						  .text(function (d) { return d; })
						  .attr("x", 0)
						  .attr("y", function (d, i) { return (i+1) * cellSize; })
						  .style("text-anchor", "left")
						  .attr("transform", "translate("+cellSize/2 + ",-6) rotate (-90)")
						  .attr("class",  function (d,i) { return "colLabel mono c"+i;} )
						  .on("mouseover", function(d) {d3.select(this).classed("text-hover",true);})
						  .on("mouseout" , function(d) {d3.select(this).classed("text-hover",false);})
						  .on("click", function(d,i) { if(enableColLabelSorter) {colSortOrder=!colSortOrder;  sortbylabel("c",i,colSortOrder);d3.select("#order").property("selectedIndex", 4).node().focus();;}})
						  ;

					  var heatMap = svg.append("g").attr("class","g3")
							.selectAll(".cellg")
							.data(_dataset, function(d){return d.row+":"+d.col;})
							.enter()
							.append("rect")
							.attr("x", function(d) { return (d.col) * cellSize; })
							.attr("y", function(d) { return (d.row) * cellSize; })
							.attr("class", function(d){return "cell cell-border cr"+(d.row-1)+" cc"+(d.col-1);})
							.attr("width", cellSize)
							.attr("height", cellSize)
							.style("fill", function(d) { return _colorMapper(d); })
							/* .on("click", function(d) {
								   var rowtext=d3.select(".r"+(d.row-1));
								   if(rowtext.classed("text-selected")==false){
									   rowtext.classed("text-selected",true);
								   }else{
									   rowtext.classed("text-selected",false);
								   }
							})*/
							.on("mouseover", function(d){
								   //highlight text
								   d3.select(this).classed("cell-hover",true);
								   d3.select(_container).selectAll(".rowLabel").classed("text-highlight",function(r,ri){ return ri==(d.row-1);});
								   d3.select(_container).selectAll(".colLabel").classed("text-highlight",function(c,ci){ return ci==(d.col-1);});
                                                                   
								   if(_tooltipHandler){
									   var html = _tooltipHandler(_options, d);
										//Update the tooltip position and value
										d3.select(_container).select("#tooltip")
											  .style("left", (d3.event.pageX+10) + "px")
											  .style("top", (d3.event.pageY-10) + "px")
											  .select("#value")
											  .html(html);
											  //.text("lables:"+rowLabel[d.row-1]+","+colLabel[d.col-1]+"\ndata:"+d.value+"\nrow-col-idx:"+d.col+","+d.row+"\ncell-xy "+this.x.baseVal.value+", "+this.y.baseVal.value);  
										//Show the tooltip
										d3.select(_container).select("#tooltip").classed("hidden", false);
								   }
								   
								   if(hoverCallback.length > 0){
								      for(var i = 0; i < hoverCallback.length; i++){
										hoverCallback[i](_options, d);
									  }
								   }
							})
							.on("mouseout", function(){
								   d3.select(this).classed("cell-hover",false);
								   d3.select(_container).selectAll(".rowLabel").classed("text-highlight",false);
								   d3.select(_container).selectAll(".colLabel").classed("text-highlight",false);
								   d3.select(_container).select("#tooltip").classed("hidden", true);
							});

					  var legend = svg.append("g")
						  .attr("transform", "translate(0," + (-(height + margin.top))+ ")")
						  .selectAll(".legend")
						  .data(_legendColorLabels)
						  .enter().append("g")
						  .attr("class", "legend");
					 
					  legend.append("rect")
						.attr("x", function(d, i) { return legendElementWidth * i; })
						.attr("y", height+(cellSize*2))
						.attr("width", legendElementWidth)
						.attr("height", cellSize)
						.style("fill", function(d, i) { return _colorBuckets[i]; });
					 
					  legend.append("text")
						.attr("class", "mono")
						.text(function(d) { return d; })
						.attr("width", legendElementWidth)
						.attr("x", function(d, i) { return legendElementWidth * i; })
						.attr("y", height + (cellSize*4));

					// Change ordering of cells

					  function sortbylabel(rORc,i,sortOrder){
						   var t = svg.transition().duration(3000);
						   var log2r=[];
						   var sorted; // sorted is zero-based index
						   svg.selectAll(".c"+rORc+i) 
							 .filter(function(ce){
								log2r.push(ce.value);
							  })
						   ;
						   if(rORc=="r"){ // sort log2ratio of a gene
							 sorted=d3.range(col_number).sort(function(a,b){ if(sortOrder){ return log2r[b]-log2r[a];}else{ return log2r[a]-log2r[b];}});
							 t.selectAll(".cell")
							   .attr("x", function(d) { return sorted.indexOf(d.col-1) * cellSize; })
							   ;
							 t.selectAll(".colLabel")
							  .attr("y", function (d, i) { return sorted.indexOf(i) * cellSize; })
							 ;
						   }else{ // sort log2ratio of a contrast
							 sorted=d3.range(row_number).sort(function(a,b){if(sortOrder){ return log2r[b]-log2r[a];}else{ return log2r[a]-log2r[b];}});
							 t.selectAll(".cell")
							   .attr("y", function(d) { return sorted.indexOf(d.row-1) * cellSize; })
							   ;
							 t.selectAll(".rowLabel")
							  .attr("y", function (d, i) { return sorted.indexOf(i) * cellSize; })
							 ;
						   }
					  }

					  svg.select("#order").on("change",function(){
						order(this.value);
					  });
					  
					  function order(value){
					   if(value=="hclust"){
						var t = svg.transition().duration(3000);
						t.selectAll(".cell")
						  .attr("x", function(d) { return (d.col) * cellSize; })
						  .attr("y", function(d) { return (d.row) * cellSize; })
						  ;

						t.selectAll(".rowLabel")
						  .attr("y", function (d, i) { return (i+1) * cellSize; })
						  ;

						t.selectAll(".colLabel")
						  .attr("y", function (d, i) { return (i+1) * cellSize; })
						  ;

					   }else if (value=="probecontrast"){
						var t = svg.transition().duration(3000);
						t.selectAll(".cell")
						  .attr("x", function(d) { return (d.col - 1) * cellSize; })
						  .attr("y", function(d) { return (d.row - 1) * cellSize; })
						  ;

						t.selectAll(".rowLabel")
						  .attr("y", function (d, i) { return i * cellSize; })
						  ;

						t.selectAll(".colLabel")
						  .attr("y", function (d, i) { return i * cellSize; })
						  ;

					   }else if (value=="probe"){
						var t = svg.transition().duration(3000);
						t.selectAll(".cell")
						  .attr("y", function(d) { return (d.row - 1) * cellSize; })
						  ;

						t.selectAll(".rowLabel")
						  .attr("y", function (d, i) { return i * cellSize; })
						  ;
					   }else if (value=="contrast"){
						var t = svg.transition().duration(3000);
						t.selectAll(".cell")
						  .attr("x", function(d) { return (d.col - 1) * cellSize; })
						  ;
						t.selectAll(".colLabel")
						  .attr("y", function (d, i) { return i * cellSize; })
						  ;
					   }
					  }
					  // 
					  var sa=svg.select(".g3")
						  .on("mousedown", function() {
							  if( !d3.event.altKey) {
								 svg.selectAll(".cell-selected").classed("cell-selected",false);
								 svg.selectAll(".rowLabel").classed("text-selected",false);
								 svg.selectAll(".colLabel").classed("text-selected",false);
							  }
							 var p = d3.mouse(this);
							 sa.append("rect")
							 .attr({
								 rx      : 0,
								 ry      : 0,
								 class   : "selection",
								 x       : p[0],
								 y       : p[1],
								 width   : 1,
								 height  : 1
							 })
						  })
						  .on("mousemove", function() {
							 var s = sa.select("rect.selection");
						  
							 if(!s.empty()) {
								 var p = d3.mouse(this),
									 d = {
										 x       : parseInt(s.attr("x"), 10),
										 y       : parseInt(s.attr("y"), 10),
										 width   : parseInt(s.attr("width"), 10),
										 height  : parseInt(s.attr("height"), 10)
									 },
									 move = {
										 x : p[0] - d.x,
										 y : p[1] - d.y
									 }
								 ;
						  
								 if(move.x < 1 || (move.x*2<d.width)) {
									 d.x = p[0];
									 d.width -= move.x;
								 } else {
									 d.width = move.x;       
								 }
						  
								 if(move.y < 1 || (move.y*2<d.height)) {
									 d.y = p[1];
									 d.height -= move.y;
								 } else {
									 d.height = move.y;       
								 }
								 s.attr(d);
						  
									 // deselect all temporary selected state objects
								 svg.selectAll('.cell-selection.cell-selected').classed("cell-selected", false);
								 svg.selectAll(".text-selection.text-selected").classed("text-selected",false);

								 svg.selectAll('.cell').filter(function(cell_d, i) {
									 if(
										 !d3.select(this).classed("cell-selected") && 
											 // inner circle inside selection frame
										 (this.x.baseVal.value)+cellSize >= d.x && (this.x.baseVal.value)<=d.x+d.width && 
										 (this.y.baseVal.value)+cellSize >= d.y && (this.y.baseVal.value)<=d.y+d.height
									 ) {
						  
										 d3.select(this)
										 .classed("cell-selection", true)
										 .classed("cell-selected", true);

										 svg.select(".r"+(cell_d.row-1))
										 .classed("text-selection",true)
										 .classed("text-selected",true);

										 svg.select(".c"+(cell_d.col-1))
										 .classed("text-selection",true)
										 .classed("text-selected",true);
									 }
								 });
							 }
						  })
						  .on("mouseup", function() {
								// remove selection frame
							 sa.selectAll("rect.selection").remove();
						  
								 // remove temporary selection marker class
							 svg.selectAll('.cell-selection').classed("cell-selection", false);
							 svg.selectAll(".text-selection").classed("text-selection",false);
						  })
						  .on("mouseout", function() {
							 if(d3.event.relatedTarget.tagName=='html') {
									 // remove selection frame
								 sa.selectAll("rect.selection").remove();
									 // remove temporary selection marker class
								 svg.selectAll('.cell-selection').classed("cell-selection", false);
								 svg.selectAll(".rowLabel").classed("text-selected",false);
								 svg.selectAll(".colLabel").classed("text-selected",false);
							 }
						  })
						  ;
					};
					
				    this.addHoverHandler = function(f){
						hoverCallback.push(f);
					}
				

				return this;
		};

})();