cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`
CONF_DIR=$DEPLOY_DIR/conf

APP="np_serial_provider"
LOG_BASE="/opt/export/log"
LOG_DIR="$LOG_BASE/$APP"
