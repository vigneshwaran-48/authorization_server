import React from "react";
import { Common } from "./Common";

const Loading = () => {

    return (
        <div className="loading-wrapper x-axis-flex">
            <img 
                src={ Common.loadingGif }
                alt="loading"
                className="loading"
            />
        </div>
    )
}

export default Loading;