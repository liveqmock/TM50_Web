<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*" %>    
<%
	String id = request.getParameter("id");
	String automailID = request.getParameter("automailID");
	String sYear = request.getParameter("sYear");
	String sMonth = request.getParameter("sMonth");
	String sDay = request.getParameter("sDay");
	String standard = request.getParameter("standard");
	standard= "statistic"+standard.substring(0,1).toUpperCase()+standard.substring(1);
	
%>

	<div class="dash_box">
		<div class="dash_box_tabs"></div>
		<h2>Bar Chart</h2>
		<div class="dash_box_content">
			<div style="height:100px;overflow-y:auto;display:none;">
<a href="javascript:$('<%=id%>').getData()" >				getdata                           </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('test.txt')" >				test.txt                           </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('area-1.txt')" >				area-1.txt                           </a><span style="width:10px">&#160;</span>  <br>
<a href="javascript:$('<%=id%>').test('area-line-dash.txt')">			area-line-dash.txt                   </a><span style="width:10px">&#160;</span>  <br>
<a href="javascript:$('<%=id%>').test('area-line.txt')">			area-line.txt                        </a><span style="width:10px">&#160;</span>  <br>
<a href="javascript:$('<%=id%>').test('area-point-objects.txt')">		area-point-objects.txt               </a><span style="width:10px">&#160;</span>  <br>
<a href="javascript:$('<%=id%>').test('bar-2.txt')">				bar-2.txt			     </a><span style="width:10px">&#160;</span>  <br>
<a href="javascript:$('<%=id%>').test('bar-3d-2-BUG.txt')">			bar-3d-2-BUG.txt                    </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('bar-3d.txt')">				bar-3d.txt                          </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('bar-4.txt')">				bar-4.txt                           </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('bar-all-onclick.txt')">			bar-all-onclick.txt                 </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('bar-alpha.txt')">			bar-alpha.txt                       </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('bar-cylinder.txt')">			bar-cylinder.txt                    </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('bar-filled.txt')">			bar-filled.txt                      </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('bar-floating-2.txt')">			bar-floating-2.txt                  </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('bar-floating.txt')">			bar-floating.txt                    </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('bar-glass-2.txt')">			bar-glass-2.txt                     </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('bar-glass-height-0.txt')">		bar-glass-height-0.txt              </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('bar-glass.txt')">			bar-glass.txt                       </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('bar-groups-with-nulls.txt')">		bar-groups-with-nulls.txt           </a><span style="width:10px">&#160;</span> <br> 
<a href="javascript:$('<%=id%>').test('bar-on-show-cascade.txt')">		bar-on-show-cascade.txt             </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('bar-on-show-pop.txt')">			bar-on-show-pop.txt                 </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('bar-on-show.txt')">			bar-on-show.txt                     </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('bar-sketch.txt')">			bar-sketch.txt                      </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('bar-test-all.txt')">			bar-test-all.txt                    </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('bug-1.txt')">				bug-1.txt                           </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('candle.txt')">				candle.txt                          </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('combined-chart-bar-line-scatter.txt')">	combined-chart-bar-line-scatter.txt </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('exp.txt')">				exp.txt                             </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('horizontal-bar-chart-2.txt')">		horizontal-bar-chart-2.txt          </a><span style="width:10px">&#160;</span> <br> 
<a href="javascript:$('<%=id%>').test('horizontal-bar-chart-3.txt')">		horizontal-bar-chart-3.txt          </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('horizontal-bar-chart.txt')">		horizontal-bar-chart.txt            </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('keys-1.txt')">				keys-1.txt                          </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('languages-chinese.txt')">		languages-chinese.txt               </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('line-2.txt')">				line-2.txt                          </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('line-anchor.txt')">			line-anchor.txt                     </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('line-bow.txt')">				line-bow.txt                        </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('line-bug-report.txt')">			line-bug-report.txt                 </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('line-dash.txt')">			line-dash.txt                       </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('line-dot.txt')">				line-dot.txt                        </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('line-hollow.txt')">			line-hollow.txt                     </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('line-on-click.txt')">			line-on-click.txt                   </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('line-on-show.txt')">			line-on-show.txt                    </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('line-star.txt')">			line-star.txt                       </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('line.txt')">				line.txt                            </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('list.txt')">				list.txt                            </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('menu.txt')">				menu.txt                            </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('on-show-area-drop.txt')">		on-show-area-drop.txt               </a><span style="width:10px">&#160;</span> <br> 
<a href="javascript:$('<%=id%>').test('on-show-area-explode.txt')">		on-show-area-explode.txt            </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('on-show-area-fade-in.txt')">		on-show-area-fade-in.txt            </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('on-show-area-mid-slide.txt')">		on-show-area-mid-slide.txt          </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('on-show-area-pop-up.txt')">		on-show-area-pop-up.txt             </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('on-show-area-shrink-in.txt')">		on-show-area-shrink-in.txt          </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('on-show-line.txt')">			on-show-line.txt                    </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('on-show-scatter-explode.txt')">		on-show-scatter-explode.txt         </a><span style="width:10px">&#160;</span> <br> 
<a href="javascript:$('<%=id%>').test('pie-1.txt')">				pie-1.txt                           </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('pie-2.txt')">				pie-2.txt                           </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('pie-3.txt')">				pie-3.txt                           </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('pie-4.txt')">				pie-4.txt                           </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('pie-bug-green.txt')">			pie-bug-green.txt                   </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('pie-colours.txt')">			pie-colours.txt                     </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('pie-fixed-radius.txt')">			pie-fixed-radius.txt                </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('pie-label-colours.txt')">		pie-label-colours.txt               </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('pie-many-slices-fixed.txt')">		pie-many-slices-fixed.txt           </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('pie-many-slices.txt')">			pie-many-slices.txt                 </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('radar-2.txt')">				radar-2.txt                         </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('radar-area.txt')">			radar-area.txt                      </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('radar-axis-and-grid-style.txt')">	radar-axis-and-grid-style.txt       </a><span style="width:10px">&#160;</span> <br> 
<a href="javascript:$('<%=id%>').test('radar-axis-labels-2.txt')">		radar-axis-labels-2.txt             </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('radar-axis-labels.txt')">		radar-axis-labels.txt               </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('radar-axis-tooltip-closest.txt')">	radar-axis-tooltip-closest.txt      </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('radar-axis-tooltip-hover.txt')">		radar-axis-tooltip-hover.txt        </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('radar-chart-minus-numbers.txt')">	radar-chart-minus-numbers.txt       </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('radar-line-loop.txt')">			radar-line-loop.txt                 </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('radar-steps.txt')">			radar-steps.txt                     </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('radar-test-keys.txt')">			radar-test-keys.txt                 </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('scatter-2.txt')">			scatter-2.txt                       </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('scatter-anchors.txt')">			scatter-anchors.txt                 </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('scatter-line-2.txt')">			scatter-line-2.txt                  </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('scatter-line-area.txt')">		scatter-line-area.txt               </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('scatter-line-step-horizontal.txt')">	scatter-line-step-horizontal.txt    </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('scatter-line-step-vertical.txt')">	scatter-line-step-vertical.txt      </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('scatter-line.txt')">			scatter-line.txt                    </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('scatter-on-click.txt')">			scatter-on-click.txt                </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('scatter-step-all.txt')">			scatter-step-all.txt                </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('scatter.txt')">				scatter.txt                         </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('shape.txt')">				shape.txt                           </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('stack-bar-1.txt')">			stack-bar-1.txt                     </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('stack-bar-2.txt')">			stack-bar-2.txt                     </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('stack-bar-on-click.txt')">		stack-bar-on-click.txt              </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('stack-bar-on-show.txt')">		stack-bar-on-show.txt               </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('stack-bar-tooltip-hover.txt')">		stack-bar-tooltip-hover.txt         </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('title-1.txt')">				title-1.txt                         </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('tooltip-1.txt')">			tooltip-1.txt                       </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('tooltip-2.txt')">			tooltip-2.txt                       </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('tooltip-bar-floating.txt')">		tooltip-bar-floating.txt            </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('tooltip-clash-scatter-2.txt')">		tooltip-clash-scatter-2.txt         </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('tooltip-clash-scatter-hover.txt')">	tooltip-clash-scatter-hover.txt     </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('tooltip-clash-scatter.txt')">		tooltip-clash-scatter.txt           </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('tooltip-clash.txt')">			tooltip-clash.txt                   </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('tooltip-hover.txt')">			tooltip-hover.txt                   </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('tooltip-mixed-1.txt')">			tooltip-mixed-1.txt                 </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('tooltip-mixed-2.txt')">			tooltip-mixed-2.txt                 </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('tooltip-rounded.txt')">			tooltip-rounded.txt                 </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('tooltip-x-radar-clash-proximity.txt')">	tooltip-x-radar-clash-proximity.txt </a><span style="width:10px">&#160;</span> <br> 
<a href="javascript:$('<%=id%>').test('tooltip-x-radar-clash.txt')">		tooltip-x-radar-clash.txt           </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('utf8-1.txt')">				utf8-1.txt                          </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('x-axis-1.txt')">				x-axis-1.txt                        </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('x-axis-auto-range.txt')">		x-axis-auto-range.txt               </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('x-axis-big.txt')">			x-axis-big.txt                      </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('x-axis-labels-2.txt')">			x-axis-labels-2.txt                 </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('x-axis-labels-3.txt')">			x-axis-labels-3.txt                 </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('x-axis-labels-4.txt')">			x-axis-labels-4.txt                 </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('x-axis-labels-5.txt')">			x-axis-labels-5.txt                 </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('x-axis-labels-6.txt')">			x-axis-labels-6.txt                 </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('x-axis-labels-7.txt')">			x-axis-labels-7.txt                 </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('x-axis-labels-angle-no-offset.txt')">	x-axis-labels-angle-no-offset.txt   </a><span style="width:10px">&#160;</span> <br> 
<a href="javascript:$('<%=id%>').test('x-axis-labels-angle-offset.txt')">	x-axis-labels-angle-offset.txt      </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('x-axis-labels.txt')">			x-axis-labels.txt                   </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('x-axis-no-offset.txt')">			x-axis-no-offset.txt                </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('x-axis-range-scatter-minus.txt')">	x-axis-range-scatter-minus.txt      </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('x-axis-range-scatter-plus.txt')">	x-axis-range-scatter-plus.txt       </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('x-axis-steps-zero-check.txt')">		x-axis-steps-zero-check.txt         </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('x-labels-auto-dates.txt')">		x-labels-auto-dates.txt             </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('x-labels-auto-min5-3rd-visible.txt')">	x-labels-auto-min5-3rd-visible.txt  </a><span style="width:10px">&#160;</span>  <br>
<a href="javascript:$('<%=id%>').test('x-labels-auto-min5.txt')">		x-labels-auto-min5.txt              </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('x-labels-auto-rev.txt')">		x-labels-auto-rev.txt               </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('x-labels-auto.txt')">			x-labels-auto.txt                   </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('x-labels-invisible-user-labels.txt')">	x-labels-invisible-user-labels.txt  </a><span style="width:10px">&#160;</span>  <br>
<a href="javascript:$('<%=id%>').test('x-labels-invisible.txt')">		x-labels-invisible.txt              </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('x-labels-user-labels-br.txt')">		x-labels-user-labels-br.txt         </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('x-labels-user-labels.txt')">		x-labels-user-labels.txt            </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('x-labels-user-x.txt')">			x-labels-user-x.txt                 </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('y-axis-big.txt')">			y-axis-big.txt                      </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('y-axis-fix-this-bug.txt')">		y-axis-fix-this-bug.txt             </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('y-axis-format-labels.txt')">		y-axis-format-labels.txt            </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('y-axis-labels-step.txt')">		y-axis-labels-step.txt              </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('y-axis-labels-user-y.txt')">		y-axis-labels-user-y.txt            </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('y-axis-no-grid.txt')">			y-axis-no-grid.txt                  </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('y-axis-no-title-no-offset-right.txt')">	y-axis-no-title-no-offset-right.txt </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('y-axis-no-title-no-offset.txt')">	y-axis-no-title-no-offset.txt       </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('y-axis-no-title-offset.txt')">		y-axis-no-title-offset.txt          </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('y-axis-offset-tiny-numbers.txt')">	y-axis-offset-tiny-numbers.txt      </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('y-axis-right-all-bars.txt')">		y-axis-right-all-bars.txt           </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('y-axis-right-all-lines-10-20.txt')">	y-axis-right-all-lines-10-20.txt    </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('y-axis-right-all-lines.txt')">		y-axis-right-all-lines.txt          </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('y-axis-right.txt')">			y-axis-right.txt                    </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('y-axis-rotate-labels.txt')">		y-axis-rotate-labels.txt            </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('y-axis-upside-down-offset.txt')">	y-axis-upside-down-offset.txt       </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('y-axis-upside-down.txt')">		y-axis-upside-down.txt              </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('y-axis.txt')">				y-axis.txt                          </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('y-labels-auto-formatted.txt')">		y-labels-auto-formatted.txt         </a><span style="width:10px">&#160;</span> <br> 
<a href="javascript:$('<%=id%>').test('y-labels-auto-labelstyle.txt')">		y-labels-auto-labelstyle.txt        </a><span style="width:10px">&#160;</span> <br>
<a href="javascript:$('<%=id%>').test('y-labels-auto.txt')">			y-labels-auto.txt                   </a><span style="width:10px">&#160;</span> <br>
			
			</div>
			<div id="<%=id%>Chart" ></div>
		</div>
		        	
	</div>
    
<script type="text/javascript">

$('<%=id%>').test = function(file) {
nemoRequest.init( 
		{
			busyWindowId: '<%=id%>',  // busy 를 표시할 window
			updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window

			url: 'pages/automail/test/'+file, 
			//update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
			onSuccess: function(html,els,resHTML,scripts) {
				//$('<%=id%>Chart').setStyles({'width':'100%', 'height':'200px'});
				$('<%=id%>Chart').style.width = '100%';
				$('<%=id%>Chart').style.height = '400px';
				$('<%=id%>Chart').load( resHTML );
			}
		}
);
nemoRequest.get({});
}


/***********************************************/
/* 검색 조건 컨트롤 초기화
/***********************************************/
	/* 리스트 표시 */
	window.addEvent("domready",function () {
		createSwfobject(
				  'swf/open-flash-chart.swf', '<%=id%>Chart',
				  '100%', '400px','9.0.0', 'swf/expressInstall.swf',
				  {'data-file': escape('automail/automail.do?method=<%=standard%>&automailID=<%=automailID%>&sYear=<%=sYear%>&sMonth=<%=sMonth%>&sDay=<%=sDay%>')});
	});

</script>