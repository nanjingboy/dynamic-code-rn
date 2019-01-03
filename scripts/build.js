const klaw = require('klaw');
const path = require('path');
const chalk = require('chalk');
const fs = require('fs-extra');
const { exec } = require('shelljs');
const sourceRootPath = path.join(__dirname, '..', 'src');
const buildDistRootPath = path.join(__dirname, '..', 'build');

async function buildBundle(dirPath) {
  const entryJSPath = path.join(dirPath, 'index.js');
  const exists = await fs.pathExists(entryJSPath);
  if (exists) {
    const version = path.basename(dirPath);
    const buildDistPath = path.join(buildDistRootPath, version);
    await fs.ensureDir(buildDistPath);
    await fs.emptyDir(buildDistPath);
    console.log(chalk.green(`Start create android bundle for V${version}...`));
    exec(
      `react-native bundle --platform android --dev false \
        --entry-file ${entryJSPath} \
        --bundle-output ${path.join(buildDistPath, `index.android.bundle`)} \
        --assets-dest ${path.join(buildDistPath, 'res.android.bundle')}`,
      { silent: true }
    );
    console.log(chalk.green(`Start create ios bundle for V${version}...`));
    exec(
      `react-native bundle --platform ios --dev false \
        --entry-file ${entryJSPath} \
        --bundle-output ${path.join(buildDistPath, `index.ios.bundle`)} \
        --assets-dest ${path.join(buildDistPath, 'res.ios.bundle')}`,
      { silent: true }
    );
  }
}

function buildBundles() {
  return new Promise((resolve, reject) => {
    klaw(sourceRootPath).on('data', file => {
      if (file.stats.isDirectory()) {
        buildBundle(file.path);
      }
    }).on('end', () => {
      resolve();
    })
  });
}

async function run() {
  await buildBundles();
}

run();