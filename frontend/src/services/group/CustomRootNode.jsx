import { useState } from 'react';
import { Handle, Position } from 'reactflow';
import "../../css/node.css";

export default function CustomRootNode({ data, isConnectable }) {
  const [isFocus, setIsFocus] = useState(false);
  return (
    <div 
    className="customNode" 
    style={isFocus ? {border:"2px solid black", backgroundColor:"#ececec"} : {border:"2px solid #777777", backgroundColor:"#ececec"}}
    onClick={()=>{setIsFocus(true)}}
    onMouseLeave={()=>{setIsFocus(false)}}
    >
      <span className='nodename'>{data.label}</span>
      <Handle type="source" position={Position.Bottom} id="b" isConnectable={isConnectable} />
    </div>
  );
}