import jwtDecode from "jwt-decode";
import RefreshToken from "../token/RefreshToken";
import configData from "../../config/config.json"
import default_profile_img from '../../img/defalutProfile.png';

export default async function AdminGetUserInfo(name) {
    await RefreshToken();
    let img;
    const accessToken = localStorage.getItem('accessToken');
    
    const downloadImage = async () => {
        // return new Promise((resolve, reject) => {
        //     const xhr = new XMLHttpRequest();
        //     xhr.responseType = 'blob';
        //     xhr.open('GET', configData.API_SERVER + 'users/profile-photo', true);
        //     xhr.onreadystatechange = () => {
        //         if (xhr.readyState === XMLHttpRequest.DONE) {
        //             const res = xhr.response;
        //             if (xhr.status !== 200) {
        //                 resolve(res);
        //             }
        //             else {
        //                 const tRes = {
        //                     "status": 200,
        //                     "data": res
        //                 }
        //                 resolve(tRes);
        //             }
        //         }
        //     }
        //     xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
        //     xhr.send();
        // });
        return null;
    }

    try {

        try {
            const downloadRes = await downloadImage();
            if (downloadRes.status !== 200 || downloadRes === null) {
                img = default_profile_img;
            }                
            img = URL.createObjectURL(downloadRes.data);
        } catch (error) {
            img = default_profile_img;
        }

        const res = await fetch(configData.API_SERVER + 'users/' + name, {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + accessToken,
            },
        })
        
        const data = await res.json();
        return [true, data, img];
    } catch (error) {
        return [false, error.message];
    }
}