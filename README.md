Jenkins TYPO3 Surf Builder
==========================

Author
------

Martin Helmich <m.helmich@mittwald.de>  
Mittwald CM Service GmbH & Co. KG

Synopsis
--------

This projects contains a simple jenkins plugin allowing to add
a TYPO3 Surf deployment as build step.

Note that this project is still in a pre-alpha state and highly
experimental.

Installation
------------

You can build the plugin using Maven:

    > mvn package

This generated an `*.hpi` file that you can upload and install
into your Jenkins installation.

Requirements
------------

This needs a working TYPO3 Flow (2.0+) installation with the TYPO3.Surf
package installed.
