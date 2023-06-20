import { ArcElement, Chart as ChartJS, Legend, Tooltip } from "chart.js";
import React, { useState } from "react";
import { Pie } from "react-chartjs-2";
import "../../css/sidebar.css";
import GetUserUsage from "../../services/user/GetUserUsage";
import GetConfig from "../../services/config/GetConfig";
import SwalError from "../swal/SwalError";

export default function Usage() {
  ChartJS.register(ArcElement, Tooltip, Legend);

  const [per, setPer] = useState(0);
  const [unit, setUnit] = useState("GB");
  const [actual, setActual] = useState(0);
  const [max, setMax] = useState(100);

  React.useEffect(() => {
    const fetchUsage = async () => {
      const res = await GetUserUsage();
      if (!res[0]) {
        return;
      }
      setActual(res[1]);
      setUnit(res[2]);
      setPer(res[3]);

      const configRes = await GetConfig("301");
      if (!configRes[0]) {
          SwalError(configRes[1]);
          return;
      }
      setMax(configRes[1]);
    }
    
    fetchUsage();
  }, []);

  const data = {
    labels: [
    ],
    datasets: [{
      data: [per, max - per],
      backgroundColor: function (context) {
        return [getColor(per, max), 'rgb(255,255,255)'];
      },
      borderColor: 'rgb(0, 0, 0)',
      borderWidth: 2,
      hoverOffset: 4
    }]
  };

  return (
    <div className="usage" >
      <Pie id="usage" data={data} />
      <div>
        <div style={{ paddingBottom: "10px", fontSize: "large" }}><b>사용량</b></div>
        <div><b>{actual}{unit}</b></div>
        <div>/ {max}GB</div>
      </div>
    </div>
  )
}

function getColor(percent, max) {
  var r = parseInt(106 + (149 * percent / max)),
    g = parseInt(236 - (70 * percent / max)),
    b = parseInt(120 + (13 * percent / max));

  var RGB = [r, g, b];
  const rgbcode = '#'
    + convertToTwoDigitHexCodeFromDecimal(RGB[0])
    + convertToTwoDigitHexCodeFromDecimal(RGB[1])
    + convertToTwoDigitHexCodeFromDecimal(RGB[2]);
  return rgbcode;
}

function convertToTwoDigitHexCodeFromDecimal(decimal) {
  var code = Math.round(decimal).toString(16);

  (code.length > 1) || (code = '0' + code);
  return code;
}