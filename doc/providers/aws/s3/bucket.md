# CloudSpec Resource Definition: aws:s3:bucket


## Properties

* **region**
(`string`):
AWS Region.
* **bucket_name**
(`string`):
Bucket name.
* **encryption**
(`nested`):
Encryption.
    * **enabled**
(`boolean`):
Encryption is enabled.
    * **type**
(`string`):
Encryption type.
* **logging**
(`nested`):
Logging.
    * **enabled**
(`boolean`):
Logging is enabled.

