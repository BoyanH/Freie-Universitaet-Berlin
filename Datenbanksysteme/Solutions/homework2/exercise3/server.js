'use strict';

/*
	
	Simple Node.js server which servers everything in the root directory

	Ecma-script 6 used here

*/

const serve_static = require('serve-static'),
	connect = require('connect'),

	port = 8050,
	serveBaseDir = serve_static(`${__dirname}/public`);


connect().use(serveBaseDir).listen(port, () => {

	var counter = 0;

	process.stdout.write(`Started server on port ${port}.\n`);

	setInterval(() => {

		process.stdout.clearLine();
    	process.stdout.cursorTo(0);
    	process.stdout.write(`Running ${counter > 0 ? (counter > 1 ? '...' : '..') : '.'}`);
		counter++;
		if(counter > 2) {
			counter = 0;
		}


	}, 500);
});