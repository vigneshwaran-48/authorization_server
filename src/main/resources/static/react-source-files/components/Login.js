import React, { useEffect, useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { RoutesManager } from "../utility/AppRoutes";
import { UserAPI } from "../api/UserAPI";

const Login = () => {
    
    const [ formData, setFormData ] = useState({
        name: "",
        password: ""
    });

    const defaultErrMsg = "User name or password is wrong";

    const [ params ] = useSearchParams();

    const [ errorObj, setErrorObj ] = useState({
        isError: false,
        errorMessage: defaultErrMsg
    });

    useEffect(() => {
        console.log(params.get("error"));
        const errorValue = params.get("error");
        setErrorObj(prev => {
            return {
                ...prev,
                isError: errorValue != null,
                errorMessage: errorValue != null ? errorValue : defaultErrMsg
            }
        });
    }, []);

    const handleFormChange = event => {
        const { name, value } = event.target;
        setFormData(prevFormData => {
            return {
                ...prevFormData,
                [ name ] : value
            }
        })
    }

    const login = async () => {
        const response = await UserAPI.login(formData);

        if(response.ok) {
            console.log("login success", response.headers);
        }
        else {
            setErrorObj({
                isError: true,
                errorMessage: "username or password is wrong"
            });
        }
    }

    return (
        <div className="login-comp y-axis-flex">
            <div className="login-comp-head x-axis-flex">   
                <img 
                    className="app-icon"
                    src="/app-icon.png"
                    alt="app-icon" />
                <h1>Login</h1>
            </div>
            <form 
                className="login-form y-axis-flex"
                action="/authenticate"
                method="POST"
            >
                <div className="input-wrapper y-axis-flex">
                    <label>Name or Email</label>
                    <input 
                        type="text" 
                        name="name"
                        placeholder="Name or Email"
                        value={formData.name}
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
                <button className="common-button login-button">Login</button>
                <p className="or-login-option">Or Login with </p>
                <div className="auth-providers-icon-wrapper x-axis-flex">
                    <a href="/oauth2/authorization/google">
                        <img 
                            className="auth-provider-icon" 
                            src="google.png" 
                            alt="auth-provider" />    
                    </a>
                </div>
                <div className="register-link-wrapper x-axis-flex">
                    <span>Don't have an account?</span>
                    <Link to={RoutesManager.signUpPage}>Register</Link>
                </div> 
            </form>
        </div>
    )
}

export default Login;