import React from "react";
import '../css/bodyframe.css'

export default function BodyFrame({children}) {
    return (
        <div className="bodyframe">
            {children}
        </div>
    )
}
