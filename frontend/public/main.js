const { app, BrowserWindow, ipcMain } = require("electron");
const path = require("path");
const isDev = require("electron-is-dev");
const context = require("./context");
const apis = require("./apis");
const fs = require('fs')

let mainWindow;

function createWindow() {
  mainWindow = new BrowserWindow({
    width: 1425,
    height: 1080,
    webPreferences: {
      nodeIntegration: true,
      enableRemoteModule: true,
      devTools: isDev,
      preload: path.join(__dirname, "preload.js")
    },
    minWidth: 1130,
    minHeight: 856,
    maxWidth: 1425,
    maxHeight: 1080,
  });
  mainWindow.setMenuBarVisibility(false);
  if (isDev) {
    mainWindow.loadURL("http://localhost:3000");  
  }
  else {
    mainWindow.loadFile(path.join(__dirname, 'index.html'))
  }

  if (isDev) {
    mainWindow.webContents.openDevTools({ mode: "detach" });
  }

  mainWindow.setResizable(true);
  mainWindow.on("closed", () => (mainWindow = null));
  mainWindow.focus();

  whenStart()
  ipcMain.handle('getFFMpegPath', context.getFFMpegPath)
  ipcMain.handle('encodeFFMpeg', handleEncode)
  ipcMain.handle('getAEScryptPath', context.getAEScryptPath)
  ipcMain.handle('encryptFile', handleEncrypt)
  ipcMain.handle('decryptFile', handleDecrypt)
}

app.on("ready", createWindow);

app.on("window-all-closed", () => {
  if (process.platform !== "darwin") {
    app.quit();
  }
});

app.on("activate", () => {
  if (mainWindow === null) {
    createWindow();
  }
});

async function whenStart() {
  const appDir = ""
  //const appDir = app.getAppPath();
  const dataDir = path.join(appDir, 'data')
  const ffmpegPath = await apis.getFFMpegPath();
  const aescryptPath = await apis.getAEScryptPath();
  fs.mkdir(dataDir, { recursive: true }, () => { })
  context.setDataDir(dataDir)
  context.setFFMpegPath(ffmpegPath)
  context.setAEScryptPath(aescryptPath)
}

async function
handleEncode(_, filepath){
  let ffmpegPath = context.getFFMpegPath();
  let dataDir = context.getDataDir();
  if (ffmpegPath === null || dataDir === null)
    return null;
  try {
    return await apis.encodeFFMpeg(ffmpegPath, dataDir, filepath)  
  } catch (e) {
    return null;
  }
  
}

async function
  handleEncrypt(_, filepath, key) {
  let aescryptPath = context.getAEScryptPath();
  if (aescryptPath == null)
    throw 'error';
  return await apis.encryptFile(aescryptPath, filepath, key);
}

async function
  handleDecrypt(_, filepath, key) {
  let aescryptPath = context.getAEScryptPath();
  if (aescryptPath == null)
    throw 'error';
  return await apis.decryptFile(aescryptPath, filepath, key);
}
