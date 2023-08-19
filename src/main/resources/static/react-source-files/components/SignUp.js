import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { UserAPI } from "../api/UserAPI";
import { RoutesManager } from "../utility/AppRoutes";

const SignUp = () => {
    
    const [ formData, setFormData ] = useState({
        userName: "",
        email: "",
        password: ""
    });

    const [ errorObj, setErrorObj ] = useState({
        isError: false,
        errorMessage: ""
    });

    const navigate = useNavigate();

    const handleFormChange = event => {
        const { name, value } = event.target;
        setFormData(prevFormData => {
            return {
                ...prevFormData,
                [ name ] : value
            }
        })
    }
    const createUser = async userData => {
        const response = await UserAPI.createUser(userData);
        if(response.ok) {
            navigate(RoutesManager.loginPage, {replace: true});
        }
        else if(response.status !== 404) {
            const responseJson = await response.json();
            showError(responseJson);
        }
        else {
            showError("Oops! Something went wrong");
        }
    }

    const handleFormSubmit = event => {
        event.preventDefault();

        createUser(formData);
    }

    const showError = error => {
        setErrorObj({
            isError: true,
            errorMessage: error
        });
    }

    return (
        <div className="login-comp y-axis-flex">
            <div className="sign-up-head x-axis-flex">   
                <img 
                    className="app-icon"
                    src={"/app-icon.png"} 
                    alt="app-icon" />
                <h1>Sign Up</h1>
            </div>
            <form 
                className="login-form y-axis-flex"
                onSubmit={handleFormSubmit}
            >
                <div className="input-wrapper y-axis-flex">
                    <label>Name</label>
                    <input 
                        type="text" 
                        name="userName"
                        placeholder="Name"
                        value={formData.userName}
                        onChange={handleFormChange}
                    />
                </div>
                <div className="input-wrapper y-axis-flex">
                    <label>Email</label>
                    <input 
                        type="text" 
                        name="email"
                        placeholder="Email"
                        value={formData.email}
                        onChange={handleFormChange}
                    />
                </div>
                <div className="input-wrapper y-axis-flex">
                    <label>Password</label>
                    <input 
                        type="password" 
                        name="password"
                        placeholder="Password"
                        value={formData.password}
                        onChange={handleFormChange}
                    />
                </div>
                <div 
                    className="error-wrapper input-wrapper y-axis-flex"
                    style={{
                        opacity: errorObj.isError ? 1 : 0
                    }}
                >
                    <label>Error</label>
                    <input
                        name="error"
                        placeholder="Oops! Something went wrong"
                        value={errorObj.errorMessage}
                        disabled
                    />
                </div>
                <button className="common-button login-button">Register</button>
                <div className="login-link-wrapper x-axis-flex">
                    <span>Already have an account?</span>
                    <Link to={RoutesManager.loginPage}>Login</Link>
                </div> 
            </form>
        </div>
    )
}

export default SignUp;