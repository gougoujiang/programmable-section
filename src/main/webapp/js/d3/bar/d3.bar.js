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
		d3.bar = function() {
				var margin = {top: 20, right: 20, bottom: 50, left: 100};
				
				var _options;
				var _container;
				var _width;
				var _height;
				var _dataset;
				var _colorMapper;
				
				this.init=function(options){
					_options = options;
					_container = options.container;
					_width = options.width;
					_height = options.height;
					_dataset = options.dataset;
					_xLabel = options.xLabel;
					_yLabel = options.yLabel;
					_colorMapper = options.colorMapper;
					if(options.margin){
						margin = options.margin;
					}
				};
				
				
				this.render = function(){

					var x = d3.scale.ordinal()
						.rangeRoundBands([0, _width], .1);

					var y = d3.scale.linear()
						.range([_height, 0]);

					var xAxis = d3.svg.axis()
						.scale(x)
						.orient("bottom");

					var yAxis = d3.svg.axis()
						.scale(y)
						.orient("left")
						.ticks(10, "%");

					var svg = d3.select(_container).append("svg")
						.attr("width", _width + margin.left + margin.right)
						.attr("height", _height + margin.top + margin.bottom)
					    .append("g")
						.attr("transform", "translate(" + margin.left + "," + margin.top + ")");
						
					x.domain(_dataset.map(function(d) { return d.label; }));
					y.domain([0, d3.max(_dataset, function(d) { return d.value; })]);

					svg.append("g")
					  .attr("class", "x axis")
					  .attr("transform", "translate(0," + _height + ")")
					  .call(xAxis)
					  .append("text")
					  .attr("y", 30)
					  .text(_xLabel);

					svg.append("g")
					  .attr("class", "y axis")
					  .call(yAxis)
					  .append("text")
					  .attr("transform", "rotate(-90)")
					  .attr("y", -50)
					  .attr("dy", ".71em")
					  .style("text-anchor", "end")
					  .text(_yLabel);

					svg.selectAll(".bar")
					  .data(_dataset)
					  .enter().append("rect")
					  .attr("class", "bar")
					  .attr("x", function(d) { return x(d.label); })
					  .attr("width", x.rangeBand())
					  .attr("y", function(d) { return y(d.value); })
					  .attr("height", function(d) { return _height - y(d.value); })
					  .attr("fill", function(d){ return _colorMapper(d); })
					  ;
				   
					function type(d) {
					  d.value = +d.value;
					  return d;
					}
				}
				
			return this;
		};

})();