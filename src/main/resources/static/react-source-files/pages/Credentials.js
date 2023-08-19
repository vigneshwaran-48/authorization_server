import React from "react";
import { Outlet } from "react-router";

const Credentials = () => {

    return (
        <div className="credential-page x-axis-flex">
            <div className="credential-left-part y-axis-flex"></div>
            <div className="credential-right-part y-axis-flex">
                <Outlet />
            </div>
        </div>
    )
}

export default Credentials;