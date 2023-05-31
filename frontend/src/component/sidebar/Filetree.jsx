import React, { useEffect, useState } from "react";
import { useNavigate } from 'react-router-dom'
import "../../css/sidebar.css"
import { FcFolder, FcFile } from 'react-icons/fc'
import GetRootDir from "../../services/directory/GetRootDir";
import GetSubFileList from "../../services/directory/GetSubFileList";
import GetSubDirList from "../../services/directory/GetSubDirList";

export default function Filetree() {

  const [datas, setDatas] = useState();
  const navigate = useNavigate();

  useEffect(() => {
    const render = async () => {

      const rootIDRes = await GetRootDir();
      if (!rootIDRes[0]) {
        return rootIDRes[1];
      }
      const rootID = rootIDRes[1];
      console.log(rootID)
      const res = await parseTree(rootID, 0);
      console.log(res);
      setDatas(res);
    }
    render();
  }, [])

  async function parseTree(folder, depth) {

    const subFileRes = await GetSubFileList(folder);
    if (!subFileRes[0]) {
      return subFileRes[1];
    }
    const subDirRes = await GetSubDirList(folder);
    if (!subDirRes[0]) {
      return subDirRes[1];
    }

    const subAll = [...subDirRes[1], ...subFileRes[1]];

    let taps = '';
    for (let index = 0; index < depth; index++) {
      taps += '   ';
    }
    if (depth !== 0) {
      taps += '└';
    }
    console.log(subAll);
    const children = await Promise.all(subAll.map(async (d) => {
      if (d.type === 'folder') {
        const subChildren = await parseTree(d.id, depth + 1);
        return (
          <div key={d.id} id={d.id} className="folder" onClick={() => { navigate("/files/" + d.id); }}>
            <span>{taps}<FcFolder />{d.name}</span>
            {subChildren}
          </div>
        )
      }
      else {
        return (
          <div key={d.id} className='file'>
            <span>{taps}<FcFile />{d.name}</span>
          </div>
        )
      }
    }));
    return children;
  }
  return (
    <div className="filetree">
      {datas}
    </div>
  )
}