import React, { useCallback, useEffect, useRef, useState } from 'react';
import { BiAddToQueue, BiSave } from 'react-icons/bi';
import ReactFlow, {
    ControlButton, Controls,
    addEdge, applyNodeChanges,
    updateEdge
} from 'reactflow';

import 'reactflow/dist/style.css';
import GetGroupTree from './GetGroupTree';
import PostCreateGroup from './PostCreateGroup';
import PostDeleteGroup from './PostDeleteGroup';

export default function Flow(props) {
    const [nodes, setNodes] = useState([]);
    const [edges, setEdges] = useState([]);
    const [prevEdges, setPrevEdges] = useState([]);

    const [isRootExist, setIsRootExist] = useState(false);

    const edgeUpdateSuccessful = useRef(true);
    const edgeRef = useRef();
    const nodeRef = useRef();
    const isRootExistRef = useRef();
    const tempRef = useRef();

    isRootExistRef.current = isRootExist;
    edgeRef.current = edges;
    nodeRef.current = nodes;

    useEffect(() => {
        const init = async () => {
            let tmpNodes = [];
            const res = await GetGroupTree();
            res.find(elem => {
                if (elem.source === "__ROOT__") {
                    tmpNodes = [{
                        id: elem.target,
                        type: 'customRootNode',
                        position: { x: 150, y: 0 },
                        data: {
                            label: elem.target,
                        },
                    }];
                    setIsRootExist(true);
                }
            });

            const tmpRes = JSON.parse(JSON.stringify(res.filter(elem => elem.source !== "__ROOT__")));
            const tmpResForNode = JSON.parse(JSON.stringify(res.filter(elem => elem.source !== "__ROOT__")));

            tmpRes.forEach((item, index, object) => {
                if (item.source === "__ROOT__") {
                    object.splice(index, 1);
                    return;
                }
                item.id = "reactflow__edge-" + item.source + '-' + item.target;
                delete item.x;
                delete item.y;
            });
            setEdges(tmpRes);
            setPrevEdges(tmpRes);

            for (let index = 0; index < tmpResForNode.length; index++) {
                const elem = tmpResForNode[index];
                tmpNodes = [...tmpNodes, {
                    id: elem.target,
                    type: 'customNode',
                    position: { x: elem.x, y: elem.y },
                    data: {
                        label: elem.target,
                    },
                    draghandle: false,
                }]
            }
            setNodes(tmpNodes);
        }
        init();
    }, [])


    return (
        <div style={{ width: 'calc(100vw - 300px)', height: 'calc(100vh - 150px)' }}>
            <ReactFlow
                panOnDrag={false}
                nodes={nodes}
                edges={edges}
                nodeTypes={props.nodeTypes}
            >
                <Controls style={{ width: "fit-content" }} showInteractive={false}>
                </Controls>
            </ReactFlow>
        </div>
    );
}