 

export default async function GetUserPwExpire(id, pw, accessToken) {

    try {
        const res = await fetch(localStorage.getItem("API_SERVER") + 'users/password/expired', {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + accessToken, // TODO: accessToken
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                "id": id,
                "password": pw,
            }),
        })
        
        const data = await res.json();
        return [true, data.changedPasswordDate];
    } catch (error) {
        return [false, error.message];
    }
}