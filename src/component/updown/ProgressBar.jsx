import React from "react";
import LinearProgress, { LinearProgressProps } from '@material-ui/core/LinearProgress'
import '../../css/load.css'
import { Box, Typography } from "@material-ui/core";

export default function ProgressBar(props){

    return (
        <div className="progressBar">
            <div>{props.name}</div>
            <LinearProgressWithLabel value={props.value} />
        </div>
    )
    
} 

function LinearProgressWithLabel(props: LinearProgressProps & { value: number }) {
    return (
      <Box sx={{ display: 'flex', alignItems: 'center' }}>
        <Box sx={{ width: '100%', mr: 1 }}>
          <LinearProgress variant="determinate" {...props} />
        </Box>
        <Box sx={{ minWidth: 35 }}>
          <Typography variant="body2" color="text.secondary">{`${Math.round(
            props.value,
          )}%`}</Typography>
        </Box>
      </Box>
    );
  }