import RefreshToken from '../token/RefreshToken';

export default async function PostCreateGroup(name, parent) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    const model = {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + accessToken,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "groupName": name,
            "parentName": parent,
        })
    };

    try {
        if (parent === "__ROOT__") {//시스템 루트 밑에는 하나만 있도록 유지
            const testRes = await fetch(localStorage.getItem("API_SERVER") + 'group/' + parent, {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + accessToken,
                },
            });
            if (testRes.ok) {
                const data = await testRes.json();
                if (data.subGroups.length !== 0) {
                    return;
                }
            }
        }
        const res = await fetch(localStorage.getItem("API_SERVER") + 'group/create', model);
        if (!res.ok) {
            const data = await res.json();
            throw data;
        }
        return [true, ''];
    } catch (data) {
        return [false, data.message];
    }
}