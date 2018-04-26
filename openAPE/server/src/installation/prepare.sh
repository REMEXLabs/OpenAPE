if [ ! -d installationfiles ]; then
	md installationfiles
	echo "Created folder installationfiles"
else 
echo "Folder installationfiles already exists"
fi

if [ -d openape-server ]; then
rm -r openape-server
echo "deleted folder openape-server"
fi

if [ -d logs ]; then
rm -R logs
mkdir logs
fi

mkdir openape-server
