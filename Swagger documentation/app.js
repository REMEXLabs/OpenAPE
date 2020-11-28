#!/usr/bin/env node
const fs = require('fs')
const express = require('express')
const pathToSwaggerUi = require('swagger-ui-dist').absolutePath()
const replace = require('replace-in-file')

const options = {
  files: `${pathToSwaggerUi}/index.html`,
  from: 'https://petstore.swagger.io/v2/swagger.json',
  to: 'https://docs.openape.gpii.eu/config/openape.yaml',
}

try {
  const results = replace.sync(options)
  console.log('Replacement results:', results)
}
catch (error) {
  console.error('Error occurred:', error)
}

const indexContent = fs.readFileSync(`${pathToSwaggerUi}/index.html`, 'utf8')

const app = express()
const port = 3013

app.use('/config', express.static(__dirname + '/config'))
app.get("/", (_req, res) => res.send(indexContent))
app.get("/index.html", (_req, res) => res.send(indexContent))
app.use(express.static(pathToSwaggerUi))

app.listen(port, () => console.log(`OpenAPE Swagger Docs listening on port ${port}!`))

