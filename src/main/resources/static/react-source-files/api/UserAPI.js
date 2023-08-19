import { ServerAPIManager } from "../utility/AppRoutes";

let isUserLoggedIn = true;

export const UserAPI = {

    createUser : async userData => {

        const response = await fetch(ServerAPIManager.resourceServerBase +
                                     ServerAPIManager.userCreateAPI, {
                                    method: "POST",
                                    mode: 'cors',
                                    headers: {
                                        "Content-Type": "application/json",
                                        "Access-Control-Allow-Origin": "*"
                                    },
                                    body: JSON.stringify(userData)
                                });
        return response;
    },
    isUserLoggedIn : () => {
        return isUserLoggedIn;
    },
    getCurrentUserDetails: async () => {
        const response = await fetch(ServerAPIManager.userInfoAPI);

        return response;
    },
    login: async userData => {

        const response = await fetch(ServerAPIManager.userLoginAPI, {
                                    method: "POST",
                                    headers: {"Content-Type": "application/json"},
                                    body: JSON.stringify(userData)
                                });
        
        return response;

    }
}