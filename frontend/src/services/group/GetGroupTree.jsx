import configData from "../../config/config.json"
import RefreshToken from '../token/RefreshToken'
import PostCreateGroup from "./PostCreateGroup";

export default async function GetGroupTree() {
    const rootName = "__ROOT__"
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    const model = {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + accessToken,
        },
    };

    const getChildren = async (parent) => {
        try {
            const res = await fetch(configData.API_SERVER + 'group/' + parent, model);
            const data = await res.json();
            if (res.status === 404) {
                return null;
            }
            else if (!res.ok) {
                throw data;
            }
            return data;
        } catch (e) {
            throw e;
        }
    }

    try {
        const findRootRes = await getChildren(rootName);
        //루트가 없으면 루트 생성
        if (findRootRes === null) { 
            const rootCreateRes = await PostCreateGroup(rootName, null);
            if (rootCreateRes[0] === false) {
                throw rootCreateRes[1];
            }
            return [];
        }
        //루트가 있는 경우 dfs
        const visited = [];
        const dfs = async (parent, depth) => {
            visited.push(parent);
            const checkChildren = await getChildren(parent);
            const children = checkChildren.subGroups;
            if (children === []) {
                return [];
            }
            const res = [];
            for (let index = 0; index < children.length; index++) {
                const child = children[index].name;
                if (visited.includes(child)) {
                    continue;
                }
                const tmp = await dfs(child, depth + 1);
                res.push({
                    "source": parent,
                    "target": child,
                    "x": (index + 1) * 150,
                    "y": depth * 150,
                });
                res.push(...tmp);
            }
            return res;
        }
        const res = await dfs(rootName, 0);
        return res;

    } catch (data) {
        return [false, data.message];
    }
}