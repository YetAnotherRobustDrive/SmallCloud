import React, { useCallback, useEffect, useRef, useState } from 'react';
import { BiAddToQueue, BiSave } from 'react-icons/bi';
import ReactFlow, {
    addEdge, applyNodeChanges, ControlButton, Controls, getConnectedEdges, getIncomers, getOutgoers, getRectOfNodes, updateEdge
} from 'reactflow';

import 'reactflow/dist/style.css';
import PostGroup from './PostGroup';
import GetGroup from './GetGroup';

export default function Flow(props) {
    const [nodes, setNodes] = useState([]);
    const [edges, setEdges] = useState([]);

    const [isRootExist, setIsRootExist] = useState(false);

    const edgeUpdateSuccessful = useRef(true);
    const edgeRef = useRef();
    const nodeRef = useRef();
    const isRootExistRef = useRef();

    isRootExistRef.current = isRootExist;
    edgeRef.current = edges;
    nodeRef.current = nodes;

    useEffect(() => {
        const init = async () => {
            const res = await GetGroup();
            const tmpRes = JSON.parse(JSON.stringify(res));
            tmpRes.forEach(e => {
                e.type = "step";
                e.id = "reactflow__edge-" + e.source + '-' + e.target;
                delete e.x;
                delete e.y;
            });
            setEdges(tmpRes);
            let tmp = [];
            for (let index = 0; index < res.length; index++) {
                const elem = res[index];
                tmp = tmp.concat({
                    id: elem.target,
                    type: 'customNode',
                    position: { x: elem.x, y: elem.y },
                    data: {
                        label: elem.target,
                    }
                })
            }

            res.map((elem) => {
                const exist = tmp.find((e) => e.id === elem.source);
                if (exist === undefined) {
                    tmp = tmp.concat({
                        id: elem.source,
                        type: 'customRootNode',
                        position: { x: 250, y: 0 },
                        data: {
                            label: elem.source,
                        },
                        dragHandle: "none"
                    })
                    return;
                }
            })
            setNodes(tmp);
        };
        init();
    }, [])

    const onConnect = useCallback((params) => {
        const exist = edgeRef.current.find((e) => e.target === params.target);
        if (exist != undefined) {
            return false;
        }
        return setEdges((eds) => addEdge({ source: params.source, target: params.target, type: "step" }, eds));
    }, [setEdges]);
    const onNodesChange = useCallback((changes) => setNodes((nds) => applyNodeChanges(changes, nds)), []);
    const onEdgesChange = useCallback((changes) => setEdges((eds) => applyNodeChanges(changes, eds)), []);
    const onNodesDelete = useCallback(
        (deleted) => {
            if (deleted[0].type == "customRootNode") {
                setIsRootExist(false);
            }
            setEdges(
                deleted.reduce((acc, node) => {
                    const incomers = getIncomers(node, nodes, edges);
                    const outgoers = getOutgoers(node, nodes, edges);
                    const connectedEdges = getConnectedEdges([node], edges);

                    const remainingEdges = acc.filter((edge) => !connectedEdges.includes(edge));

                    const createdEdges = incomers.flatMap(({ id: source }) =>
                        outgoers.map(({ id: target }) => ({ source, target, type: "step" }))
                    );

                    return [...remainingEdges, ...createdEdges];
                }, edges)
            );
        },
        [nodes, edges]
    );
    const onEdgeUpdateStart = useCallback(() => {
        edgeUpdateSuccessful.current = false;
    }, []);
    const onEdgeUpdate = useCallback((oldEdge, newConnection) => {
        edgeUpdateSuccessful.current = true;
        setEdges((els) => updateEdge(oldEdge, newConnection, els));
    }, []);
    const onEdgeUpdateEnd = useCallback((_, edge) => {
        if (!edgeUpdateSuccessful.current) {
            setEdges((eds) => eds.filter((e) => e.id !== edge.id));
        }
        edgeUpdateSuccessful.current = true;
    }, []);

    const handleClickAdd = () => {
        const name = window.prompt("새 그룹의 이름을 입력해주세요.");
        const exist = nodeRef.current.find((e) => e.id === name);
        if (exist != undefined) {
            return;
        }

        if (isRootExistRef.current == true) {
            const newNode = nodes.concat({
                id: name,
                type: 'customNode',
                position: { x: 0, y: 0 },
                data: {
                    label: name,
                }
            })
            setNodes(newNode);
            return;
        }
        else {
            const newNode = nodes.concat({
                id: name,
                type: 'customRootNode',
                position: { x: 250, y: 0 },
                data: {
                    label: name,
                },
                dragHandle: "none"
            })
            setIsRootExist(true);
            setNodes(newNode);
            return;
        }
    }

    const handleClickSave = async () => {
        const tmpEdge = JSON.parse(JSON.stringify(edgeRef.current));
        const tmpNode = JSON.parse(JSON.stringify(nodeRef.current));
        const position = tmpNode.map((a) => [a.id, a.position.x, a.position.y]);
        
        tmpEdge.forEach(e => {
            const pos = position.find((elem) => elem[0] === e.target);
            e.x = pos[1];
            e.y = pos[2];
            delete e.type;
            delete e.id;
        })
        await PostGroup(tmpEdge);//XY mapping
    }

    return (
        <div style={{ width: 'calc(100vw - 220px)', height: 'calc(100vh - 75px)' }}>
            <ReactFlow
                nodes={nodes}
                edges={edges}
                onNodesChange={onNodesChange}
                onEdgesChange={onEdgesChange}
                snapToGrid
                onEdgeUpdate={onEdgeUpdate}
                onEdgeUpdateStart={onEdgeUpdateStart}
                onEdgeUpdateEnd={onEdgeUpdateEnd}
                onConnect={onConnect}
                nodeTypes={props.nodeTypes}
                onNodesDelete={onNodesDelete}
            >
                <Controls style={{ width: "fit-content" }} showInteractive={false}>
                    <ControlButton onClick={handleClickAdd}>
                        <div><BiAddToQueue /></div>
                    </ControlButton>
                    <ControlButton onClick={handleClickSave}>
                        <div><BiSave /></div>
                    </ControlButton>
                </Controls>
            </ReactFlow>
        </div>
    );
}