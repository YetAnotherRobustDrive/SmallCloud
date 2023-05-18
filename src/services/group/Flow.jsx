import React, { useCallback, useRef, useState } from 'react';
import { BiAddToQueue, BiSave } from 'react-icons/bi';
import ReactFlow, {
    addEdge, applyNodeChanges, ControlButton, Controls, getConnectedEdges, getIncomers, getOutgoers, updateEdge
} from 'reactflow';

import 'reactflow/dist/style.css';

export default function Flow(props) {
    const [nodes, setNodes] = useState([]);
    const [edges, setEdges] = useState([]);

    const edgeUpdateSuccessful = useRef(true);
    const currEdge = useRef();
    currEdge.current = edges;
    const currNode = useRef();
    currNode.current = nodes.length;

    const onConnect = useCallback((params) => {
        const exist = currEdge.current.find((e) => e.target === params.target);
        if (exist != undefined) {
            return false;
        }
        return setEdges((eds) => addEdge({ source: params.source, target: params.target, type: "step" }, eds));
    }, [setEdges]);
    const onNodesChange = useCallback((changes) => setNodes((nds) => applyNodeChanges(changes, nds)), []);
    const onEdgesChange = useCallback((changes) => setEdges((eds) => applyNodeChanges(changes, eds)), []);
    const onNodesDelete = useCallback(
        (deleted) => {
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
        if (currNode.current != 0) {
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
                position: { x: 0, y: 0 },
                data: {
                    label: name,
                }
            })
            setNodes(newNode);
            return;
        }
    }

    const handleClickSave = () => {
        console.log(currEdge.current)
        console.log(currNode.current)
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