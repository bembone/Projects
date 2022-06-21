#include <stdio.h>
#include <string.h>
#define MAXNAMN 20
#define MAXANTALSIZE 10
#define MAXPRODUCTS 10000

typedef struct{
    char name[MAXNAMN+1]; // +1 för NULL tecken
    int size[MAXANTALSIZE];
    int amount[MAXANTALSIZE];
    int usedsize;
} Product;

void viewProduct(Product *pProduct);

void viewInventory(Product *pProductInv,int InvSize);

Product newProduct(Product *pProductInv,int *pInvSize);

int findProduct(Product *pProductInv, int InvSize, int *pInvPos);

void addProductSize(Product *pProductInv, int InvSize, int InvPos);

void adjustInventory(Product *pProductInv, int InvSize, int InvPos);

void viewLimit(Product *pProduct,int limit);

void lowAmounts(Product *pProductInv);

void deleteProduct(Product *pProductInv, int *pInvSize, int InvPos);

int fileCheck(char *fname);

int main(int argc, char **argv)
{
    printf("Lab 3 Inventory Program\n");
    Product ProductInventory[MAXPRODUCTS];
    int inventorysize;
    int InventoryPosition;
    char filename[FILENAME_MAX];
    FILE * pFile;
    printf("Enter a file to access or create one: ");
    scanf("%s",filename);
    if (fileCheck(filename)==1){             // Om filen finns
        pFile = fopen(filename,"r");
        fscanf(pFile, "%d", &inventorysize); // läser in hur många producter det finns
        for(int i=0;i<inventorysize;i++){
            char ProdName[MAXNAMN+1];
            fscanf(pFile, "%d",&ProductInventory[i].usedsize); // läser in hur många sizes det finns av en specifik product i taget
            fscanf(pFile, "%s",ProdName); // läser in namnet av den producten + efterliggande 'space'
            strcpy(ProductInventory[i].name,ProdName);
            for(int j=0;j<ProductInventory[i].usedsize;j++){ // läser in sizes och amounts av dessa sizes
                fscanf(pFile, "%d %d",&ProductInventory[i].size[j],&ProductInventory[i].amount[j]);
            }
        }
        fclose(pFile);
    }
	char cmd;
    do{
        cmd='0';
        printf("cmd: ");
        scanf(" %c",&cmd);
        switch (cmd){
            case 'n' : 
            case 'v' : 
            case 'f' : 
            case 'a' :
            case 'i' :
            case 'd' :
            case 'l' :
            case 'q' : cmd=cmd-'a'+'A'; break;  // Omvandla till stor bokstav om dessa är små
            default  : break;
        }
        switch (cmd){
            case 'N' : ProductInventory[inventorysize]=(newProduct(ProductInventory,&inventorysize)); break;
            case 'V' : viewInventory(ProductInventory,inventorysize); break;
            case 'F' : findProduct(ProductInventory,inventorysize,&InventoryPosition); break; 
            case 'A' : addProductSize(ProductInventory,inventorysize,InventoryPosition); break;
            case 'I' : adjustInventory(ProductInventory,inventorysize,InventoryPosition); break;
            case 'D' : deleteProduct(ProductInventory,&inventorysize,InventoryPosition); break;
            case 'L' : lowAmounts(ProductInventory); break;
            case 'Q' : printf("Exit Invenory!\n"); break;
            default  : printf("use : new/view/find/add/inventory/delete/low/quit!\n");
        }
    }while(cmd!='Q');
    pFile = fopen(filename,"w"); // write mode, nedan sparas all data till filen som användaren skrev i början
    fprintf(pFile,"%d\n",inventorysize); 
    for(int i=0;i<inventorysize;i++){
        fprintf(pFile,"%d%s\n",ProductInventory[i].usedsize,ProductInventory[i].name);
        for(int j=0;j<ProductInventory[i].usedsize;j++){
            fprintf(pFile,"%d %d\n",ProductInventory[i].size[j],ProductInventory[i].amount[j]);
        }
    }
    fclose(pFile);
	printf("...Lab #3 done, bye!\n");
	return 0;
}

void viewProduct(Product *pProduct){
    printf("%s\n",(*pProduct).name);
    printf("\t\tsize   :");
    for(int i=0;(i<(*pProduct).usedsize);i++){
        printf("   %3d",(*pProduct).size[i]);
    } printf("\n");
    printf("\t\tamount :");
    for(int i=0;(i<(*pProduct).usedsize);i++){
        printf("   %3d",(*pProduct).amount[i]);
    } printf("\n");
}

void viewInventory(Product *pProductInv,int InvSize){
    for(int i=0;i<InvSize && (pProductInv[i].usedsize)!=0;i++){
        viewProduct(&pProductInv[i]);
    }
}

Product newProduct(Product *pProductInv,int *pInvSize){
    char newName[MAXNAMN+1];
    for(int i=0;i<MAXNAMN;i++){
        newName[i]=' ';
    }
    int newSize;
    int usedSize=0;
    int check; 
    Product newProd;
    if ((*pInvSize)<MAXPRODUCTS){
        do{
            check=1;
            printf("new : name ? ");
            scanf("%s",newName);
            for(int i=0;i<*pInvSize;i++){
                if (strcmp(pProductInv[i].name,newName)==0){
                    check=0;
                    printf("Product already exists!\n");
                }
            }
        }while(check==0);
        for(int i=0;newSize!=0 && i<MAXANTALSIZE;i++){
            printf("new : size (or 0) ? ");
            scanf("%d",&newSize);
            newProd.size[i]=newSize;
            newProd.amount[i]=0;
            usedSize++;
            if (newSize==0){
                usedSize--; // när använderan anget size som 0 så tas den bort
            }
        }
        *pInvSize=*pInvSize+1; // en till product har laggts till
        strcpy(newProd.name,newName);
        newProd.usedsize=usedSize;
        return newProd;
    } else printf("err : No room for another product!\n");
    return;
}

int findProduct(Product *pProductInv, int InvSize, int *pInvPos){
    char search[MAXNAMN+1];
    int results=0;
    printf("fnd : look for ? ");
    scanf("%s",search);
    for(int i=0;i<InvSize;i++){
        if (strstr(pProductInv[i].name,search)!=NULL){
            viewProduct(&pProductInv[i]);
            results++;
            *pInvPos=i;
        }
    }
    return results; // returnerar hur många resultat som sökningen fick
}

void addProductSize(Product *pProductInv, int InvSize, int InvPos){
    int check=0;
    int newSize;
    for (int i=0;check!=1;i++){
        check=findProduct(pProductInv,InvSize,&InvPos);
        if (check==1){
            for(int j=0;newSize!=0 && (pProductInv[InvPos].amount[(pProductInv[InvPos].usedsize)])<MAXANTALSIZE;j++){
                printf("add : size (or 0) ? ");
                scanf("%d",&newSize);
                pProductInv[InvPos].size[(pProductInv[InvPos].usedsize)]=newSize;
                pProductInv[InvPos].amount[(pProductInv[InvPos].usedsize)]=0;
                (pProductInv[InvPos].usedsize)++;
                    if (newSize==0){
                        (pProductInv[InvPos].usedsize)--;
                }
            }
        }
    }
}

void adjustInventory(Product *pProductInv, int InvSize, int InvPos){
    int check=0;
    int newAmount;
    for (int i=0;check!=1;i++){
        check=findProduct(pProductInv,InvSize,&InvPos);
        if (check==1){
            for(int j=0;j<(pProductInv[InvPos].usedsize);j++){
                printf("inv : Adjust %4d package with (+/-) ? ",(pProductInv[InvPos].size[j]));
                scanf("%d",&newAmount);
                pProductInv[InvPos].amount[j]=(pProductInv[InvPos].amount[j])+newAmount;
                if ((pProductInv[InvPos].amount[j])<0){
                    printf("err : Underflow, amount set to zero!\n");
                    pProductInv[InvPos].amount[j]=0;
                }
            }
        }
    }
}

void viewLimit(Product *pProduct,int limit){
    int check=0;
    for(int i=0;i<MAXPRODUCTS && (*pProduct).usedsize!=0 && check==0;i++){
        if((*pProduct).amount[i]<=limit && i<(*pProduct).usedsize){
            printf("%s\n",(*pProduct).name);
            printf("\t\t size   :");
            check++;
        }
    }
    for(int i=0;(i<(*pProduct).usedsize);i++){
        if(((*pProduct).amount[i])<=limit)
            printf("   %3d",(*pProduct).size[i]);
    } if (check!=0){
        printf("\n");
    }
    check=0;
    for(int i=0;i<MAXPRODUCTS && (*pProduct).usedsize!=0 && check==0;i++){
        if((*pProduct).amount[i]<=limit && i<(*pProduct).usedsize){
            printf("\t\t amount :");
            check++;
        }
    }
    for(int i=0;(i<(*pProduct).usedsize);i++){
        if(((*pProduct).amount[i])<=limit)
            printf("   %3d",(*pProduct).amount[i]);
    } if (check!=0){
        printf("\n");
    }
}

void lowAmounts(Product *pProductInv){
    int limit;
    printf("low : Low Limit ? ");
    scanf("%d",&limit);
    for(int i=0;i<MAXPRODUCTS && (pProductInv[i].usedsize)!=0;i++){
        viewLimit(&pProductInv[i],limit);
    }
}

void deleteProduct(Product *pProductInv, int *pInvSize, int InvPos){
    int check=0;
    char decision;
    //char nameReset[MAXNAMN+1]={'0'};
    for (int i=0;check!=1;i++){
        check=findProduct(pProductInv,*pInvSize,&InvPos);
        if (check==1){
            printf("del : Delete %s (y/n) ? ",pProductInv[InvPos].name);
            scanf(" %c",&decision);
        }
    }
    if(decision=='y'){
        for(int j=InvPos;j<(*pInvSize)-1;j++){ // flyttar alla element efter positionen av producten som ska bort ett steg "bakåt"
            pProductInv[j]=pProductInv[j+1];
        }
        (*pInvSize)--; // vi har tagit bort en product
    }
}


int fileCheck(char *fname){
    FILE *pFile;
    if ((pFile = fopen(fname,"r"))){
        fclose(pFile);
        return 1; // Om filen finns returnera 1
    }else return 0;
}
