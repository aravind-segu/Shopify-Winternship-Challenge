#############################
##Shopify Backend Challenge##
########Aravind Segu#########
#############################

import urllib, json
import pandas as pd

outputData = {}
pageNumber = 1
invalidCustomers = []

#The First While Loop Iterates through the various pages
while(True):
    requirements = pd.DataFrame(columns = ('field','required', 'type', 'lengthMin', 'lengthMax'))
    #Json Data is Fetched from url
    url = "https://backend-challenge-winter-2017.herokuapp.com/customers.json?page=" + str(pageNumber)
    urlResponse = urllib.urlopen(url)
    jsonData = json.loads(urlResponse.read())
    validations = jsonData["validations"]
    customers = jsonData["customers"]
    if (len(customers) == 0):
        break
    customerFields = []
    #Validations are retrieved and stored in a pandas data frame
    for field in validations:
        for req in field:
            customerFields.append(req)
            reqRow = []
            reqRow.append(req)
            try:
                reqRow.append(field[req]['required'])
            except:
                reqRow.append("false")
            try:
                reqRow.append(field[req]['type'])
            except:
                reqRow.append("every")
            try:
                reqRow.append(field[req]['length']['min'])
            except:
                reqRow.append("-1")
            try:
                reqRow.append(field[req]['length']['max'])
            except:
                reqRow.append("-1")
            requirements.loc[len(requirements)] = reqRow
    requirements.set_index('field', inplace=True)
    #Customers data is cross checked and invalid data is stored into a dictionary
    for field in customers:
        dict = {}
        invalidFields = []
        for val in field:
            try:
                localReqs = requirements.loc[val]
            except:
                continue
            value = field[val]
            if value is None:
                if localReqs["required"] == True:
                    invalidFields.append(str(val))
            if value != None:
                if localReqs['type'] != "every":
                    if localReqs['type'] == "string":
                        if type(value) != unicode:
                            invalidFields.append(str(val))
                    if localReqs['type'] == "boolean":
                        if type(value) != bool:
                            invalidFields.append(str(val))
                    if localReqs['type'] == "number":
                        if type(value) != int:
                            invalidFields.append(str(val))
                if localReqs['lengthMin'] != "-1":
                    if len(str(value)) < localReqs['lengthMin']:
                        invalidFields.append(str(val))
                if localReqs['lengthMax'] != "-1":
                    if len(str(value)) > localReqs['lengthMax']:
                        invalidFields.append(str(val))
        if len(invalidFields) != 0:
            dict["id"] = field["id"]
            dict["invalid_fields"] = invalidFields
            invalidCustomers.append(dict)
    pageNumber += 1
#Output Data is converted to JSON Object
outputData ["invalid_customers"] = invalidCustomers
jsonOutput = json.dumps(outputData)
print jsonOutput

