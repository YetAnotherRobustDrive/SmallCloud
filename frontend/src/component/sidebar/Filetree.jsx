import React, { useEffect, useState } from "react";
import { useNavigate } from 'react-router-dom'
import "../../css/sidebar.css"
import { FcFolder, FcFile } from 'react-icons/fc'

import FT_folder from '../../fakeJSON/filetree.json'
import FT_file from '../../fakeJSON/filetree_th.json'
import FT_middle from '../../fakeJSON/filetree_sec.json'

export default function Filetree() {

  const [datas, setDatas] = useState();
  const navigate = useNavigate();

  function parseTree(folder, depth) {
    if (folder == 0) { //remove before apply fetch
      folder = FT_folder
    }
    else if (folder == 8) {
      folder = FT_file
    }
    else {
      folder = FT_middle
    }

    let taps = '';
    for (let index = 0; index < depth; index++) {
      taps += '   ';
    }
    if (depth != 0) {
      taps += '└';
    }

    return folder.map((d) => {
      if (d.type == 'folder') {
        //fetch here
        return (
          <div key={d.id} id={d.id} className="folder" onClick={() => { navigate("/files/" + d.id); }}>
            <span>{taps}<FcFolder />{d.name}</span>
            {parseTree(d.id, depth + 1)}
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
    })
  }

  useEffect(() => {
    setDatas(parseTree(0, 0));
  }, [])

  return (
    <div className="filetree">
      {datas}
    </div>
  )
}