import React, { useEffect, useState } from "react";
import Header from "../../component/header/Header";
import SidebarMypage from "../../component/sidebar/SidebarMypage";
import BodyFrame from "../../component/Bodyframe";
import BodyHeader from "../../component/main/BodyHeader";
import GetLoginLog from "../../services/log/GetLoginLog";

export default function SecurityInfoPage() {

    const [log, setLog] = useState([
        {
            time: "2021-08-01 12:00:00",
            user: "admin",
            action: "로그인",
            status: "성공",
            ip: "111.111.111.111",
        },
    ]);

    useEffect(() => {
        // const init = async () => {
        //     const res = await GetLoginLog();
        //     setLog(res);
        // }
        // init();
    }, []);

    return (
        <>
            <Header />
            <SidebarMypage />
            <BodyFrame>
                <BodyHeader text="로그인 내역" />
                <div className="logTable">
                    <table>
                        <thead>
                            <tr>
                                <th>시간</th>
                                <th>닉네임</th>
                                <th>종류</th>
                                <th>결과</th>
                                <th>IP</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                log.map((item, index) => (
                                    <tr key={index}>
                                        <td>{item.time}</td>
                                        <td>{item.user}</td>
                                        <td>{item.action}</td>
                                        <td>{item.status}</td>
                                        <td>{item.ip}</td>
                                    </tr>
                                ))
                            }
                        </tbody>
                    </table>
                </div>
            </BodyFrame>
        </>
    )
}
