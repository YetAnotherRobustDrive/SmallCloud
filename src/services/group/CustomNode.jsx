import { useState } from 'react';
import { Handle, Position } from 'reactflow';
import "../../css/node.css";

export default function CustomNode({ data, isConnectable }) {
  const [isFocus, setIsFocus] = useState(false);
  return (
    <div 
    className="customNode" 
    style={isFocus ? {border:"2px solid black"} : {border:"2px solid #777777"}}
    onClick={()=>{setIsFocus(true)}}
    onMouseLeave={()=>{setIsFocus(false)}}
    >
      <Handle type="target" position={Position.Top} isConnectable={isConnectable} />
      <span className='nodename'>{data.label}</span>
      <Handle type="source" position={Position.Bottom} id="b" isConnectable={isConnectable} />
    </div>
  );
}