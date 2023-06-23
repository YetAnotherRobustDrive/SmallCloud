
const which = require('which');
const path = require('path');
const { spawn } = require('child_process');
const { chdir } = require('process');
const { stat, readdir, readFile, existsSync, unlink, writeFile } = require('fs');

async function
  getFFMpegPath() {
  return await which('ffmpeg', { nothrow: true });
}

async function
  getAEScryptPath() {
  return await which('aescrypt', { nothrow: true });
}

async function
  encodeFFMpeg(ffmpegPath, resDir, filepath) {
  const filename = path.parse(filepath).name
  const initSegName = `init-${filename}$RepresentationID$.$ext$`
  const mediaSegName = `chunk-${filename}$RepresentationID$-$Number%05d$.$ext$`
  const outname = path.join(filename + '.mpd')
  const args = [`-i`, filepath, `-map`, `0`, `-c:a`, `aac`, `-c:v`, `libx264`,
    `-profile:v`, `main`, `-vf`, `scale=iw:ih`,
    `-bf`, `1`, `-keyint_min`, `60`, `-g`, `60`, `-sc_threshold`,
    `0`, `-b_strategy`, `0`, `-seg_duration`, `2`,
    `-use_timeline`, `1`, `-init_seg_name`, initSegName,
    `-media_seg_name`, mediaSegName, `-use_template`, `1`, `-f`,
    `dash`, outname]  

  return await new Promise((resolve, reject) => {
    try {
      const pwd = process.cwd();
      console.log("pwd", pwd);
      chdir(resDir)
      const ffmpeg = spawn(ffmpegPath, args)
      ffmpeg.stdout.on('data', (data) => {
        console.log(data.toString())
      })
      ffmpeg.stderr.on('data', (data) => {
        console.error(data.toString())
      })
      ffmpeg.on('close', (code) => {
        chdir(pwd);
        resolve(code);
      })
      
    } catch (e) {
      reject(e);
    }
  }).then((_) => {
    return new Promise((resolve, reject) => {
      stat(path.join(resDir, outname), (err, _) => {
        if (err !== null) {
          console.log("stat err", err);
          console.log("stat", _);
          reject(err);
        }
        else
          resolve(path.join(resDir, outname));
      })
    })
  }).then((mpdPath) => {
    return new Promise((resolve, reject) => {
      console.log("mpdPath", mpdPath);
      readdir(resDir, (err, files) => {
        if (err !== null)
          reject(err);
        else
          resolve({
            success: true,
            mpdPath,
            files: files
              .filter((file) => file.startsWith(`init-${filename}`) ||
                file.startsWith(`chunk-${filename}`))
          });
      })
    })
  });
}

async function
  getFromLocal(filepath) {
    console.log("getFromLocal", filepath);
  return new Promise((resolve, reject) => {
    try {
      if (!existsSync(filepath)) {
        reject(new Error('File not found'));
      }
      readFile(filepath, (err, data) => {
        if (err !== null)
          reject(err);
        else
          resolve(new Blob([data]));
      })
    }
    catch (e) {
      reject(e);
    }
  })
}

async function
  clearFolder(folderPath) {
  return new Promise((resolve, reject) => {
    try {
      readdir(folderPath, (err, files) => {
        if (err !== null)
          reject(err);
        else {
          files.forEach((file) => {
            unlink(path.join(folderPath, file), (err, _) => {
              if (err !== null)
                reject(err);
            })
          })
          resolve(true);
        }
      })
    } catch (e) {
      reject(e);
    }
  }) 
}

async function
 rmLocalFile(filepath) {
  return new Promise((resolve, reject) => {
    try {
      unlink(filepath, (err, _) => {
        if (err !== null)
          reject(err);
        else
          resolve(true);
      })
    } catch (e) {
      reject(e);
    }
  })
}

async function
  saveBlob(filepath, blob) {
  return new Promise((resolve, reject) => {
    try {
      const reader = new FileReader();
      reader.onload = () => {
        writeFile(filepath, Buffer.from(reader.result), (err) => {
          if (err !== null)
            reject(err);
          else
            resolve(true);
        })
      }
      reader.readAsArrayBuffer(blob);
    } catch (e) {
      reject(e);
    }
  })
}

async function
  encryptFile(aescryptPath, filepath, key) {
  const outname = `${filepath}.aes`

  const args = [`-e`, `-p`, key, filepath];

  return await new Promise((resolve, reject) => {
    try {
      const ffmpeg = spawn(aescryptPath, args)
      ffmpeg.on('close', (_) => {
        resolve('');
      })
    } catch (e) {
      reject(e);
    }
  }).then((_) => {
    return new Promise((resolve, reject) => {
      if (path === null || path === undefined) {
        reject('path/improper');
      } else {
        stat(outname, (err, _) => {
          if (err == null) {
            resolve({ success: true, path: outname })
          } else {
            resolve({ success: false, path: '' })
          }
        })
      }
    })
  });
}

async function
  decryptFile(aescryptPath, filepath, key) {
  const pat = /(.*).aes/;
  const outnameM = filepath.match(pat);
  let outname;
  if (outnameM !== null && outnameM.length > 0) {
    outname = outnameM[0];
  }
  const args = [`-d`, `-p`, key, filepath];
  return await new Promise((resolve, reject) => {
    try {
      const ffmpeg = spawn(aescryptPath, args)
      ffmpeg.on('close', (_) => {
        resolve('');
      })
    } catch (e) {
      reject(e);
    }
  }).then(_ => {
    return new Promise((resolve, reject) => {
      if (path === null || path === undefined) {
        reject('path/improper');
      } else {
        stat(outname, (err, _) => {
          if (err == null) {
            resolve({ success: true, path: outname })
          } else {
            resolve({ success: false, path: '' })
          }
        })
      }
    })
  });
}

exports.getFFMpegPath = getFFMpegPath;
exports.encodeFFMpeg = encodeFFMpeg;
exports.getFromLocal = getFromLocal;
exports.getAEScryptPath = getAEScryptPath;
exports.encryptFile = encryptFile;
exports.decryptFile = decryptFile;
exports.rmLocalFile = rmLocalFile;
exports.clearFolder = clearFolder;
exports.saveBlob = saveBlob;