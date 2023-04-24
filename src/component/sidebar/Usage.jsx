import React, { useState } from "react";
import "../../css/sidebar.css"
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
import { Pie } from "react-chartjs-2";

export default function Usage(){

  ChartJS.register(ArcElement, Tooltip, Legend);

  const [per, setPer] = useState(0);

  function tmpFunc(e){
    setPer(e.target.value)
  }
  const data = {
    labels: [
    ],
    datasets: [{
      data: [per, 100-per],
      backgroundColor: function(context) {
          const chart = context.chart;
          const {_, chartArea} = chart;
          if (!chartArea) {
            // This case happens on initial chart load
            return;
          }
          return [getColor(per), 'rgb(255,255,255)'];
        },
      borderColor: 'rgb(0, 0, 0)',
      borderWidth: 2,
      hoverOffset: 4
    }]
  };

  return (
    <div className="usage" >
      <Pie id="usage" data={data}/>
      <div>
        <div>사용량</div>
        <div><b>{per}</b></div>
        <div>/100GB</div>
      </div>
      <input style={{width:"50px"}} onChange={tmpFunc}></input>
    </div>
  )
}

function getColor(percent) {
  var r = parseInt(106 + (149 * percent/100)),
      g = parseInt(236 - (70 * percent/100)),
      b = parseInt(120 + (13 * percent/100));

  var RGB = [r, g, b];
  const rgbcode = '#'
      + convertToTwoDigitHexCodeFromDecimal(RGB[0])
      + convertToTwoDigitHexCodeFromDecimal(RGB[1])
      + convertToTwoDigitHexCodeFromDecimal(RGB[2]);
  return rgbcode;
}

function convertToTwoDigitHexCodeFromDecimal(decimal){
    var code = Math.round(decimal).toString(16);

    (code.length > 1) || (code = '0' + code);
    return code;
}