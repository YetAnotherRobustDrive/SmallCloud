#!/usr/bin/perl

use Digest::SHA qw(sha256_hex);

package main;

# 상수
use constant {
	PREFIX => "SMALLCLOUD",
	SECRET => "SECRET",
	ORIGIN => "ORIGIN",
	BACKEND_PORT => "BACKEND_PORT",
	FRONTEND_PORT => "FRONTEND_PORT",
	ADDRESS => "ADDRESS",
	ADMIN_USERNAME => "ADMIN_USERNAME",
	ADMIN_PASSWORD => "ADMIN_PASSWORD",
	DB_URL => "DB_URL",
	DB_DATABASE => "DB_DATABASE",
	DB_PORT => "DB_PORT",
	DB_USERNAME => "DB_USERNAME",
	DB_PASSWORD => "DB_PASSWORD",
	DB_ROOT_PASSWORD => "DB_ROOT_PASSWORD",
	MINIO_URL => "MINIO_URL",
	MINIO_PORT => "MINIO_PORT",
	MINIO_ADMIN_PORT => "MINIO_ADMIN_PORT",
	MINIO_USER => "MINIO_USER",
	MINIO_PASSWORD => "MINIO_PASSWORD",
	MINIO_BUCKET_NAME => "MINIO_BUCKET_NAME"
};

# env key and value
my %data;

sub put_value {
	my ($desc, $key) = @_;
	print $desc;
	$value = <STDIN>;
	chomp($value);
	$data{$key} = $value
}

# generate jwt key
put_value("secret을 만들기 위한 문자열을 아무거나 입력하세요: ", SECRET);
$data{SECRET} = sha256_hex($data{SECRET});

# front
put_value("frontend의 port를 입력하시오: ", FRONTEND_PORT);
$data{ADDRESS} = "0.0.0.0";
$data{ORIGIN} = sprintf "http://localhost:%s", $data{FRONTEND_PORT};

# backend
put_value("backend의 port를 입력하시오: ", BACKEND_PORT);
put_value("admin의 id를 입력하시오: ", ADMIN_USERNAME);
put_value("admin의 password를 입력하시오: ", ADMIN_PASSWORD);

# database
put_value("db의 port를 입력하시오: ", DB_PORT);
put_value("db의 database 이름을 입력하시오: ", DB_DATABASE);
$data{DB_URL} = sprintf "jdbc:mariadb://db:%s/%s?serverTimezone=Asia/Seoul", $data{DB_PORT}, $data{DB_DATABASE};
put_value("db의 root유저의 password를 입력하시오: ", DB_ROOT_PASSWORD);
put_value("db의 username을 입력하시오: ", DB_USERNAME);
put_value("db의 user의 password를 입력하시오: ", DB_PASSWORD);

# minio
put_value("minio의 port를 입력하시오: ", MINIO_PORT);
put_value("minio의 admin용 port를 입력하시오: ", MINIO_ADMIN_PORT);
put_value("minio의 bucket 이름을 입력하시오: ", MINIO_BUCKET_NAME);
put_value("minio의 유저 이름을 입력하시오: ", MINIO_USER);
put_value("minio의 유저 패스워드를 입력하시오: ", MINIO_PASSWORD);
$data{MINIO_URL} = sprintf "http://minio:%s", $data{MINIO_PORT};

open (ENV_FILE, ">.env") or die "파일을 열 수 없습니다";
foreach my $key (keys %data) {
	$value = %data{$key};
	$key = PREFIX . "_" . $key;
	print ENV_FILE "$key=$value\n";
}
