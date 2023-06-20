 
import default_logo_img from "../../img/defaultLogo.png";
export default async function GetLogo() {
    let img = default_logo_img;

    const downloadImage = async () => {
        return new Promise((resolve, reject) => {
            const xhr = new XMLHttpRequest();
            xhr.responseType = 'blob';
            xhr.open('GET', localStorage.getItem("API_SERVER") + 'users/profile-photo', true);
            xhr.onreadystatechange = () => {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    const res = xhr.response;
                    if (xhr.status !== 200) {
                        resolve(res);
                    }
                    else {
                        const tRes = {
                            "status": 200,
                            "data": res
                        }
                        resolve(tRes);
                    }
                }
            }
            xhr.send();
        });
    }

    try {
        try {
            const downloadRes = await downloadImage();
            if (downloadRes.status !== 200 || downloadRes === null) {
                img = default_logo_img;
            }                
            img = URL.createObjectURL(downloadRes.data);
        } catch (error) {
            img = default_logo_img;
        }
        return img;
    } catch (error) {
        return img;
    }
}