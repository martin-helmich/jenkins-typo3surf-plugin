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

License
-------

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
