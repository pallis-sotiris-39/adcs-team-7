# adcs-team-7

## How to run this project in your local machine:

- Clone the repo
- Run `docker-compose up -d` in the root directory
- Wait for all docker containers to run
- Build and run the `main()` function of the `sensors` module using gradle

After these steps the sensors start producing data and this data is stored in `influxdb`.

We had a problem with connecting grafana to our influxdb even though they're both running successfully.

You can see all the data in influxdb UI with these steps:

- Open your browser
- Go to `http://localhost:8086`
- Use credentials `capyuser` and `capypassword` for the username and password fields respectively
- Navigate to the Data Explorer section.
- Check bucket named `sensorbucket` using the time range of 25/12/2020 - 05/01/2022 (to include all late events)
