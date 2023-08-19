import React from "react";

const Error404 = () => {
    
    return (
        <div className="error-404 y-axis-flex">
            <div className="error404-header x-axis-flex">
                <h1>ProApp</h1>
            </div>
            <div className="error404-img-wrapper y-axis-flex">
                <img src="error404.png" alt="404 error"/>
                <h1>Oops! That page not found.</h1>
            </div>
            <div className="error404-footer"></div>
        </div>
    )
}

export default Error404;