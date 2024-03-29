const { ipcRenderer, contextBridge } = require('electron')
const { getFromLocal, rmLocalFile, clearFolder, saveBlob } = require('./apis')

const api = {
  getFFMpegPath: () => ipcRenderer.invoke('getFFMpegPath'),
  encodeFFMpeg: async (filepath) => ipcRenderer.invoke('encodeFFMpeg', filepath),
  getFromLocal: async (filepath) => getFromLocal(filepath),
  rmLocalFile: async (filepath) => rmLocalFile(filepath),
  clearFolder: async (folderPath) => clearFolder(folderPath),
  saveBlob: async (filepath, blob) => saveBlob(filepath, blob),
  getAEScryptPath: () => ipcRenderer.invoke('getAEScryptPath'),
  encryptFile: async (aescryptPath, filepath, key) => ipcRenderer.invoke('encryptFile', aescryptPath, filepath, key),
  decryptFile: async (aescryptPath, filepath, key) => ipcRenderer.invoke('decryptFile', aescryptPath, filepath, key),
}

contextBridge.exposeInMainWorld('electron', api)
