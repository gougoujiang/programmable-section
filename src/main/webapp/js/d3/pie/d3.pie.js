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
		d3.pie= function() {
				var margin = {top: 20, right: 20, bottom: 50, left: 40};
				color = d3.scale.category20c();     //builtin range of colors
				
				var _options;
				var _container;
				var _dataset;				
				var _innerR;
				var _outerR;				
				var _width;
				var _height;
				var _label;
				
				this.init=function(options){
					_options = options;
					_container = options.container;
					
					_innerR = 0;
					if(options.innerR){
						_innerR = options.innerR;
					}
					_outerR = options.outerR;
					_width = options.width;
					_height = options.height;
					_label = options.label;
					
					if(options.margin){
						margin = options.margin;
					}
					
					// Draw data from large to small, to avoid label text cut
					_dataset = _.chain(options.dataset)
					              .filter(function(d){ return d.value != 0 })
								  .sortBy(function(d){return d.value;})
								  .value().reverse();
				};
				
				
				this.render=function(){

					var svg = d3.select(_container)
							  .append('svg')
							  .attr('width', _width + margin.left + margin.right)
							  .attr('height', _height + margin.top + margin.bottom)
							  .append('g')
							  .attr('transform', 'translate(' + (_width + margin.left + margin.right) / 2 + 
								',' + (_height + margin.top + margin.bottom) / 2 + ')');
					
					var arc = d3.svg.arc()
					  .outerRadius(_outerR)
					  .innerRadius(_innerR);
					  
					var pie = d3.layout.pie()
					  .value(function(d) { return d.value; })
					  .sort(null);
										
				    var g = svg.selectAll(".arc")
					           .data(pie(_dataset))
					           .enter().append("g")
					           .attr("class", "arc");

					g.append("path")
					 .attr("d", arc)
					 .style("fill", function(d, i) { return color(i); });

					g.append("text")
					 .attr("transform", function(d) { return "translate(" + arc.centroid(d) + ")"; })
					 .attr("dy", ".35em")
					 .style("text-anchor", "middle")
					 .text(function(d) { return d.data.label + ':' + (d.data.value * 100).toFixed(2) + '%'; });
				
				   // The label
				    svg.append('text')
				     .attr('y', _outerR + 15)
					 .attr('dy', '.35em')
					 .attr('class', 'label')
					 .style('text-anchor', 'middle')
					 .text(_label);
					   
			    };
				
			return this;
		};

})();