#!/usr/bin/env zsh

#SDL Error Management Tests

#get path to jest-graphql-test module
currentPath=`pwd`;
echo $currentPath

#go to sdl-extension-files
cd "$currentPath/src/test/resources/sdl-extension-files";

#find sdl resources
sdlextensions=`pwd`;
echo 'SDL test resources folder found ' $sdlextensions;

#find sdl test module
sdltestmoduleone="$currentPath/sdl-example-one";
sdltestmoduletwo="$currentPath/sdl-example-two";

echo 'SDL example module one was found ' $sdltestmoduleone;
echo 'SDL example module one was found ' $sdltestmoduletwo;

cd $sdlextensions;

sdlTests=( $( ls . ) );

echo 'SDL tests found ' $sdlTests;

if [ "$1" = "1" ]; then

	cd $sdlextensions/$sdlTests[1]/;
	echo `pwd`;

	sdltestfile=( $( ls . ) );
	echo 'sdl test file was found ' $sdltestfile;

	cp $sdltestfile $sdltestmoduleone/src/main/resources/META-INF/;

elif [[ "$1" = "2" ]]; then

	cd $sdlextensions/$sdlTests[2]/;
	echo `pwd`;

	sdltestfile=( $( ls . ) );
	echo 'sdl test file was found ' $sdltestfile;

	cp $sdltestfile $sdltestmoduleone/src/main/resources/META-INF/;

elif [[ "$1" = "3" ]]; then

	cd $sdlextensions/$sdlTests[3]/;
	echo `pwd`;

	sdltestfile=( $( ls . ) );
	echo 'sdl test file was found ' $sdltestfile;

	cp $sdltestfile $sdltestmoduleone/src/main/resources/META-INF/;

elif [[ "$1" = "4" ]]; then

	cd $sdlextensions/$sdlTests[4]/;
	echo `pwd`;

	sdltestfile=( $( ls . ) );
	echo 'sdl test file was found ' $sdltestfile;

	cp $sdltestfile $sdltestmoduleone/src/main/resources/META-INF/;

elif [[ "$1" = "5" ]]; then

	cd $sdlextensions/$sdlTests[5]/;

	echo `pwd`;

	sdltestfile=( $( ls . ) );

	echo 'sdl test file was found ' $sdltestfile;

	cp $sdltestfile $sdltestmoduleone/src/main/resources/META-INF/;

else
	echo 'no sdl test file was chosen';
fi


